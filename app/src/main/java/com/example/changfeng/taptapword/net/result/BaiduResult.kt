package com.example.changfeng.taptapword.net.result

/**
 * Created by changfeng on 2016/3/17.
 */
class BaiduResult {

    var erro: String? = null
    var error_msg: String? = null

    private val from: String? = null
    private val to: String? = null
    private var trans_result: List<Result>? = null

    class Result {
        private val src: String? = null
        private val dst: String? = null

        override fun toString() =
                try {
                    "$src $dst;"
                } catch (e: Exception) {
                    ""
                }
    }

    val result: String
        get() = if (trans_result == null) "" else {
            "百度翻译 " + if (trans_result != null) {
                "\n" + trans_result!!.joinToString("\n")
            } else ""
        }

    companion object {

        const val TAG = "BaiduResult"

        const val ERROR_CODE_SUCCESS = "52000"
        const val ERROR_CODE_REQUEST_TIME_OUT = "52001"
        const val ERROR_CODE_SYSTEM_ERROR = "52002"
        const val ERROR_CODE_UNAUTHORIZED_USER = "52003"
        const val ERROR_CODE_REQUEIRED_PARAMETER_IS_NULL = "54000"
        const val ERROR_CODE_CLIENT_IP_ILLIGAL = "58000"
        const val ERROR_CODE_SIGNATURE_ERROR_ = "54001"
        const val ERROR_CODE_FREQUENCY_ACCESS_RESTRICTED = "54003"
        const val ERROR_CODE_NOT_SURPORT_TARGET_LANGUAGE = "58001"
        const val ERROR_CODE_INSUFFICIENT_FUNDS = "54004"
        const val ERROR_CODE_LONG_QUERY_TOO_FREQUENT = "54005"
    }
}
