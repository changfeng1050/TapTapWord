package com.example.changfeng.taptapword.net.result

import com.google.gson.annotations.SerializedName


/**
 * Created by changfeng on 2016/3/17.
 */
class YoudaoResult {

    private var errorCode: Int = 0
    private var translation: List<String>? = null
    private var basic: Basic? = null
    private var web: List<Web>? = null

    val ukPhonetic: String
        get() = if (basic?.ukPhonetic != null) basic!!.ukPhonetic!! else ""


    val usPhonetic: String
        get() = if (basic?.usPhonetic != null) basic!!.usPhonetic!! else ""

    class Basic {
        val phonetic: String? = null
        @SerializedName("uk-phonetic")
        val ukPhonetic: String? = null
        @SerializedName("us-phonetic")
        val usPhonetic: String? = null
        val explains: List<String>? = null

        override fun toString(): String {
            return try {
                "美:[$usPhonetic] 英:[$ukPhonetic]\n\n${explains!!.joinToString("\n")}"
            } catch (e: NullPointerException) {
                ""
            }
        }
    }

    class Web {
        private val key: String? = null
        private val value: List<String>? = null

        override fun toString(): String {
            val result = StringBuilder()
            result.append(key).append(" ")
            for (i in value!!.indices) {
                result.append(value[i] + ";")
            }
            return result.toString()
        }
    }

    val translateResult: String
        get() = if (errorCode != ERROR_CODE_SUCCESS) {
            ""
        } else {
            if (translation != null) {
                "有道翻译\n${translation!!.joinToString("\n")}"
            } else
                ""
        }

    val parsedExplains: String
        get() = try {
            "有道词典\n${basic!!.explains!!.joinToString(";\n")}"
        } catch (e: NullPointerException) {
            ""
        }

    val parseWebTranslate: String
        get() = try {
            "网络释义\n${web!!.joinToString("\n")}"
        } catch (e: NullPointerException) {
            ""
        }

    val formatPhones: String
        get() = if (usPhonetic.isNullOrBlank()) {
            ""
        } else {
            "美:[$usPhonetic] "
        } + if (ukPhonetic.isNullOrBlank()) {
            ""
        } else {
            "英:[$usPhonetic]"
        }

    companion object {
        const val TAG = "YoudaoResult"

        //    errorCode：
        // 　0 ­ 正常
        // 　20 ­ 要翻译的文本过长
        // 　30 ­ 无法进行有效的翻译
        // 　40 ­ 不支持的语言类型
        // 　50 ­ 无效的key
        // 　60 ­ 无词典结果，仅在获取词典结果生效
        const val ERROR_CODE_SUCCESS = 0
        const val ERROR_CODE_TEXT_TOO_LONG = 20
        const val ERROR_CODE_CANNOT_TRANSLATE_PROPER = 30
        const val ERROR_CODE_NOT_NOT_SUPPORTED_LANGUAGE = 40
        const val ERROR_CODE_INVALID_KEY = 50
        const val ERROR_CODE_NO_RESULT_IN_DICT = 60
    }
}
