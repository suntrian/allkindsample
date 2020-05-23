package com.suntr.antlr.function.define

import com.suntr.antlr.function.define.FunctionDefine
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object FunctionConfigParser {
    private const val DEFAULT_FUNCTION_CONFIG_PATH = "/function/"
    private const val DEFAULT_FUNCTION_CONFIG_FILE = "function.lst"
    private var configDirPath: String = DEFAULT_FUNCTION_CONFIG_PATH
    //类别的pattern
    private val sectionPattern = Pattern.compile("\\[(.*?)]")
    //行注释
    private val lineCommentPattern = Pattern.compile("\\s*(?:#|//).*$")
    //块注释
    private val blockCommentPattern = Pattern.compile("\\s*/\\*(.*?)\\*/", Pattern.DOTALL)
    //函数定义及转SQL
    private val functionPatternMultiLine  = Pattern.compile("^\\s*(?<func>\\S+?)\\s*\\((?<params>.*?)\\)\\s*:\\s*(?<ret>\\S+)\\s*->\\s*(?<sql>'''.*?)?$", Pattern.CASE_INSENSITIVE)
    private val functionPatternSingleLine = Pattern.compile("^\\s*(?<func>\\S+?)\\s*\\((?<params>.*?)\\)\\s*:\\s*(?<ret>\\S+)\\s*->\\s*(?!(?:\\s+|'''))(?<sql>.+)\\s*$", Pattern.CASE_INSENSITIVE)
    //函数参数
    private val paramsPattern = Pattern.compile("(?<vararg>vararg )?\\s*(?<name>\\S+?)\\s*:\\s*(?<type>(?:String|Double|Integer|Bool|Date|DateTime|ANY))\\s*(?::\\s*\\[(?<option>.*?)\\])?", Pattern.CASE_INSENSITIVE)
    //函数返回类型
    private val functionReturnTypePattern = Pattern.compile("(Integer|Double|Bool|String|Date|Time|DateTime|UNKNOWN|ANY)", Pattern.CASE_INSENSITIVE)
    //函数返回类型由第N个参数的返回类型决定
    private val functionReturnParamTypeIndexPattern = Pattern.compile("#(?<idx>\\d+).type", Pattern.CASE_INSENSITIVE)


    //缓存函数清单
    private var defaultFunctionsMapBySections: MutableMap<String, MutableList<FunctionDefine>> = LinkedHashMap()

    //函数列表
    @JvmOverloads
    @JvmStatic
    fun listFunctions(): List<FunctionDefine> {
        return mergeWithDefault().values.stream().flatMap { obj: List<FunctionDefine>? -> obj!!.stream() }.distinct().collect(Collectors.toList())
    }

    @JvmOverloads
    @JvmStatic
    fun mapFunctionsByName(): Map<String, List<FunctionDefine>> {
        return mergeWithDefault().values.stream().flatMap { obj: List<FunctionDefine>? -> obj!!.stream() }.distinct()
                .collect(Collectors.groupingBy({ obj: FunctionDefine -> obj.name },
                        { TreeMap<String, List<FunctionDefine>>(java.lang.String.CASE_INSENSITIVE_ORDER) },
                        Collectors.toList()))
    }

    @JvmStatic
    @JvmOverloads
    fun mapFunctionsBySection(): Map<String, List<FunctionDefine>?>? {
        return mergeWithDefault()
    }

    private fun parse() {
        if (defaultFunctionsMapBySections.isEmpty()) {
            synchronized(defaultFunctionsMapBySections) {
                if (defaultFunctionsMapBySections.isEmpty()) {
                    defaultFunctionsMapBySections = _parse(DEFAULT_FUNCTION_CONFIG_PATH + DEFAULT_FUNCTION_CONFIG_FILE)
                }
            }
        }
    }

    private fun mergeWithDefault(): MutableMap<String, MutableList<FunctionDefine>> {
        if (defaultFunctionsMapBySections.isEmpty()) {
            parse()
        }
        var tempFunctionDefine = defaultFunctionsMapBySections
        return tempFunctionDefine
    }

    private fun merge(defaultFuncMap: MutableMap<String, MutableList<FunctionDefine>>, dialectFuncMap: MutableMap<String, MutableList<FunctionDefine>>) :MutableMap<String, MutableList<FunctionDefine>> {
        if (dialectFuncMap.isEmpty()) {
            return defaultFuncMap;
        }
        val defaultCopy = defaultFuncMap.entries.map { it.key to ArrayList(it.value) as MutableList<FunctionDefine> }.toMap(LinkedHashMap())
        dialectFuncMap.forEach{(k: String, v: MutableList<FunctionDefine>)->
            if (defaultCopy.containsKey(k)) {
                defaultCopy[k]!!.removeAll(v)
                defaultCopy[k]!!.addAll(v)
            } else{
                defaultCopy[k] = v
            }
        }
        return defaultCopy;
    }

    private fun _parse(configFilePath: String): MutableMap<String, MutableList<FunctionDefine>> {
        var functionsMapBySections: MutableMap<String, MutableList<FunctionDefine>> = LinkedHashMap()
        var inStream: InputStream? = FunctionConfigParser::class.java.getResourceAsStream(configFilePath) ?: return functionsMapBySections
        val bufferedReader = BufferedReader(InputStreamReader(inStream!! , StandardCharsets.UTF_8))
        var line: String? = ""
        var curSections = arrayOf("default")
        var comment = ""
        while (bufferedReader.readLine().also { line = it } != null) {
            var matcher: Matcher = sectionPattern.matcher(line);
            if (sectionPattern.matcher(line).also { matcher = it }.matches()) {
                val sections = matcher.group(1)
                curSections = sections.split(",").toTypedArray()
                for (section in curSections) {
                    functionsMapBySections[section] = ArrayList()
                }
            } else if (lineCommentPattern.matcher(line).matches()) {
                //do nothing
            } else if (functionPatternSingleLine.matcher(line).also { matcher = it }.matches()) {
                val function = parseFunctionDefine(matcher)
                val sqlFunc = matcher.group("sql")
                function.sqlMapping = parseSqlMapping(sqlFunc)
                function.comment = comment
                function.sections = Arrays.asList(*curSections)
                comment = ""
                for (section in curSections) {
                    functionsMapBySections[section]!!.add(function)
                }
            } else if (functionPatternMultiLine.matcher(line).also { matcher = it }.matches()) {
                val function = parseFunctionDefine(matcher)
                var sqlFunc : String? = matcher.group("sql")
                val builder = StringBuilder()
                if (sqlFunc == null) {
                    line = bufferedReader.readLine()
                    do {
                        if (!line.isNullOrBlank() && !lineCommentPattern.matcher(line).matches()){
                            builder.append(line)
                        }
                    } while (bufferedReader.readLine().also { line = it } != null && !line!!.contains("'''"))
                    builder.append(line)
                    readSqlMapping(builder, bufferedReader)
                } else if (!sqlFunc.trim().matches("'''.+?'''$".toRegex())){
                    builder.append(sqlFunc)
                    readSqlMapping(builder, bufferedReader)
                } else{
                    builder.append(sqlFunc)
                }
                sqlFunc = builder.trim().substring(3, builder.length-3);
                function.sqlMapping = parseSqlMapping(sqlFunc)
                function.comment = comment
                function.sections = Arrays.asList(*curSections)
                comment = ""
                for (section in curSections) {
                    functionsMapBySections[section]!!.add(function)
                }
            } else if (line!!.trim { it <= ' ' }.startsWith("/*")) {
                val builder = StringBuilder(line).append("\n")
                while (bufferedReader.readLine().also { line = it } != null && !line!!.contains("*/")) {
                    builder.append(line).append("\n")
                }
                if (line != null) {
                    builder.append(line)
                }
                if (blockCommentPattern.matcher(builder.toString()).also { matcher = it }.find()) {
                    comment = matcher.group(1)
                    comment = comment.replace("(^|(?!=\n))[ \t]*\\*".toRegex(), "$1").trim { it <= ' ' }
                }
            }
        }
        return functionsMapBySections
    }

    private fun readSqlMapping( builder: StringBuilder, bufferedReader: BufferedReader) {
        var line: String? = ""
        while (bufferedReader.readLine().also { line = it } != null && !line!!.contains("'''")) {
            if (!lineCommentPattern.matcher(line).matches()){
                builder.append(line)
            }
        }
        if (line != null) {
            builder.append(line)
        }
    }

    private fun parseFunctionDefine(matcher: Matcher): FunctionDefine {
        val funcName = matcher.group("func")
        val params = matcher.group("params")
        val funcType = matcher.group("ret")
        val function = FunctionDefine()
        function.name = funcName
        function.params = parseParams(params)
        if (functionReturnTypePattern.matcher(funcType).matches()) {
            function.type = DataType.of(funcType)
        } else  {
            val indexMatcher: Matcher = functionReturnParamTypeIndexPattern.matcher(funcType);
            if (indexMatcher.matches()){
                val typeParamIndex = Integer.parseInt(indexMatcher.group("idx"))
                if (typeParamIndex > function.params.size){
                    throw IllegalStateException("返回类型的定义超出函数参数个数")
                }
                function.typeParamIndex = typeParamIndex;
            } else{
                throw IllegalStateException("未识别的函数返回类型：$funcType");
            }
        }
        return function
    }

    private fun parseParams(params: String?): List<FunctionDefine.ParamDefine> {
        if (params == null || params.isEmpty()) {
            return emptyList()
        }
        val paramArray = params.split(",").toTypedArray()
        val paramDefines: MutableList<FunctionDefine.ParamDefine> = ArrayList(paramArray.size)
        var matcher: Matcher? = null
        for (i in paramArray.indices) {
            val p = paramArray[i]
            if (paramsPattern.matcher(p).also { matcher = it }.matches()) {
                val vararg = matcher!!.group("vararg")
                check(!(i != paramArray.size - 1 && vararg != null)) { "可变参数只能是最后一个参数" }
                val name = matcher!!.group("name")
                val type = matcher!!.group("type")
                val optionStr = matcher!!.group("option")
                var options: List<String> = Collections.emptyList()
                if (optionStr!=null) {
                     options = parseParamOptions(optionStr)
                }
                val define = FunctionDefine.ParamDefine(vararg != null, name, DataType.of(type), options)
                paramDefines.add(define)
            }
        }
        return paramDefines
    }

    private fun parseParamOptions(text: String): List<String> {
        return text.split("|").toList()
    }

    private fun parseSqlMapping(text: String): FunctionDefine.FunctionSqlMapping {
        return FunctionDefine.FunctionSqlMapping(text)
    }

}