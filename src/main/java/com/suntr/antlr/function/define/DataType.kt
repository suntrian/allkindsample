package com.suntr.antlr.function.define

import java.io.Serializable

enum class DataType : Serializable {
    DOUBLE, INTEGER, STRING, BOOL, DATE, TIME, DATETIME, UNKNOWN, ANY;

    fun isCompatible(type: DataType): Boolean {
        if (ANY == this || ANY == type) {
            return true
        }
        if (UNKNOWN == this || UNKNOWN == type) {
            return true
        }
        if (DOUBLE == this || INTEGER == this) {
            return DOUBLE == type || INTEGER == type
        }
        if (DATE == this) {
            return DATE == type || DATETIME == type
        }
        return if (DATETIME == this) {
            DATE == type || TIME == type || DATETIME == type
        } else this == type
    }

    fun isDate(): Boolean {
        return Companion.isDate(this)
    }

    fun isNumber(): Boolean {
        return isNumber(this)
    }

    companion object {

        @JvmStatic
        fun of(type: String?): DataType {
            if (type == null) return STRING
            val uType = type.toUpperCase();
            return try {
                valueOf(uType)
            } catch (e: IllegalArgumentException) {
                when (uType) {
                    "FLOAT", "DECIMAL" -> DOUBLE
                    "BOOLEAN", "BIT" -> BOOL
                    "TIMESTAMP" -> DATETIME
                    else -> {
                        when {
                            uType.contains("NUM") -> DOUBLE
                            uType.contains("INT") -> INTEGER
                            uType.contains("TIME") -> DATETIME
                            uType.contains("DATE") -> DATE
                            uType.contains("CHAR") -> STRING
                            uType.contains("STRING") -> STRING
                            uType.contains("TEXT")  -> STRING
                            uType.contains("DEC") -> DOUBLE
                            else -> UNKNOWN
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun isDate(type: DataType?) : Boolean {
            return when (type) {
                null -> false
                TIME, DATE, DATETIME -> true
                else -> false
            }
        }

        @JvmStatic
        fun isNumber(type: DataType?) : Boolean {
            return when (type) {
                null -> false
                DOUBLE, INTEGER -> true
                else -> false
            }
        }
    }
}