package com.example.changfeng.taptapword.net.result

import com.squareup.moshi.Json

/**
 * Created by changfeng on 2016/3/17.
 */
class YoudaoResult {

    var errorCode: Int = 0
    var translation: List<String>? = null
    var basic: Basic? = null
    var web: List<Web>? = null

    val ukPhonetic: String?
        get() = if (basic != null && basic!!.usPhonetic != null) basic!!.ukPhonetic else ""


    val usPhonetic: String?
        get() = if (basic != null && basic!!.usPhonetic != null) basic!!.usPhonetic else ""

    class Basic {
        val phonetic: String? = null
        @Json(name = "uk-phonetic")
        val ukPhonetic: String? = null
        @Json(name = "us-phonetic")
        val usPhonetic: String? = null
        val explains: List<String>? = null

        override fun toString(): String {
            val result = StringBuilder()
            try {
                result.append("UK:[").append(ukPhonetic).append("] ").append("US:[").append(usPhonetic).append(']')
            } catch (e: NullPointerException) {

            }

            result.append("\n")
            for (explain in explains!!) {
                result.append("\n").append(explain)
            }
            return result.toString()
        }
    }

    class Web {
        val key: String? = null
        val values: List<String>? = null

        override fun toString(): String {
            val result = StringBuilder()
            result.append(key).append(" ")
            for (i in values!!.indices) {
                result.append(values[i] + ";")
            }
            return result.toString()
        }
    }

    val dictResult: String
        get() {
            if (errorCode != ERROR_CODE_SUCCESS) {
                return ""
            }
            if (basic != null) {
                return "有道词典\n" + basic!!.toString()
            } else {
                return ""
            }
        }

    val webInterpretationResult: String
        get() {
            if (errorCode != ERROR_CODE_SUCCESS) {
                return ""
            }

            if (web != null) {
                val result = StringBuilder()
                result.append("网络释义")
                for (w in web!!) {
                    result.append("\n").append(w.toString())
                }
                return result.toString()
            } else {
                return ""
            }
        }

    val translateResult: String
        get() {
            if (errorCode != ERROR_CODE_SUCCESS) {
                return ""
            }
            val result = StringBuilder()

            if (translation != null) {
                result.append("有道翻译")
                for (t in translation!!) {
                    result.append("\n").append(t)
                }
            }
            return result.toString()
        }


    val result: String
        get() {
            if (errorCode != ERROR_CODE_SUCCESS) {
                return ""
            }
            val result = StringBuilder()
            val dictResult = dictResult
            val webInterpretationResult = webInterpretationResult
            val translateResult = translateResult

            if (!dictResult.isEmpty()) {
                result.append("\n\n").append(dictResult)
            }
            if (!webInterpretationResult.isEmpty()) {
                result.append("\n\n").append(webInterpretationResult)
            }
            if (!translateResult.isEmpty()) {
                result.append("\n\n").append(translateResult)
            }

            return result.toString()
        }

    val parsedExplains: String
        get() {
            try {
                val explainString = StringBuilder()
                basic?.explains?.let {
                    for (i in 0..basic!!.explains!!.size - 1) {
                        if (i != 0) {
                            explainString.append("\n")
                        }
                        explainString.append(basic!!.explains?.get(i) + ";")
                    }
                }

                return explainString.toString()
            } catch (e: NullPointerException) {
                return ""
            }

        }

    val parseWebTranslate: String
        get() {
            try {
                val webTranslate = StringBuilder()
                for (i in web!!.indices) {
                    if (i != 0) {
                        webTranslate.append("\n")
                    }
                    webTranslate.append(web!![i])
                }
                return webTranslate.toString()
            } catch (e: NullPointerException) {
                return ""
            }

        }

    val formatPhones: String
        get() = if(usPhonetic.isNullOrBlank()) "" else "美:[$usPhonetic] " + if (ukPhonetic.isNullOrBlank()) "" else "英:[$usPhonetic]"

    companion object {
        val TAG = "YoudaoResult"

        //    errorCode：
        // 　0 ­ 正常
        // 　20 ­ 要翻译的文本过长
        // 　30 ­ 无法进行有效的翻译
        // 　40 ­ 不支持的语言类型
        // 　50 ­ 无效的key
        // 　60 ­ 无词典结果，仅在获取词典结果生效
        val ERROR_CODE_SUCCESS = 0
        val ERROR_CODE_TEXT_TOO_LONG = 20
        val ERROR_CODE_CANNOT_TRANSLATE_PROPER = 30
        val ERROR_CODE_NOT_NOT_SUPPORTED_LANGUAGE = 40
        val ERROR_CODE_INVALID_KEY = 50
        val ERROR_CODE_NO_RESULT_IN_DICT = 60
    }
}
