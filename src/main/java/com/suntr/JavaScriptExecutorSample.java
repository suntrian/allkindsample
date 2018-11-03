package com.suntr;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;

public class JavaScriptExecutorSample {

    public static void main(String[] args) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("print('hello world'); console.log('dafasfasd')");
        //engine.eval(new FileReader("file.js"));
    }

}
