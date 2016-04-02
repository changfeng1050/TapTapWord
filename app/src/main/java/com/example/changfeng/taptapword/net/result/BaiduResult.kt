package com.example.changfeng.taptapword.net.result

/**
 * Created by changfeng on 2016/3/17.
 */
class BaiduResult {

    var erro: String? =null
    var error_msg: String ?= null

    private val from: String? = null
    private val to: String? = null
    internal var trans_result: List<Result>? = null

    internal inner class Result {
        val src: String? = null
        val dst: String? = null
    }

    val result: String
        get() =
            if (trans_result != null) {
                "百度翻译 " + if (trans_result != null) {"\n" + trans_result!!.joinToString("\n")} else ""
            } else ""

    companion object {

        val TAG = "BaiduResult"

        val ERROR_CODE_SUCCESS = "52000"
        val ERROR_CODE_REQUEST_TIME_OUT = "52001"
        val ERROR_CODE_SYSTEM_ERROR = "52002"
        val ERROR_CODE_UNAUTHORIZED_USER = "52003"
        val ERROR_CODE_REQUEIRED_PARAMETER_IS_NULL = "54000"
        val ERROR_CODE_CLIENT_IP_ILLIGAL = "58000"
        val ERROR_CODE_SIGNATURE_ERROR_ = "54001"
        val ERROR_CODE_FREQUENCY_ACCESS_RESTRICTED = "54003"
        val ERROR_CODE_NOT_SURPORT_TARGET_LANGUAGE = "58001"
        val ERROR_CODE_INSUFFICIENT_FUNDS = "54004"
        val ERROR_CODE_LONG_QUERY_TOO_FREQUENT = "54005"
    }
}
