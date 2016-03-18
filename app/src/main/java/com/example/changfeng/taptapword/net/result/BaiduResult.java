package com.example.changfeng.taptapword.net.result;

import java.util.List;

/**
 * Created by changfeng on 2016/3/17.
 */
public class BaiduResult {

    public static final String TAG = "BaiduResult";

    public static final String ERROR_CODE_SUCCESS = "52000";
    public static final String ERROR_CODE_REQUEST_TIME_OUT = "52001";
    public static final String ERROR_CODE_SYSTEM_ERROR = "52002";
    public static final String ERROR_CODE_UNAUTHORIZED_USER = "52003";
    public static final String ERROR_CODE_REQUEIRED_PARAMETER_IS_NULL = "54000";
    public static final String ERROR_CODE_CLIENT_IP_ILLIGAL = "58000";
    public static final String ERROR_CODE_SIGNATURE_ERROR_ = "54001";
    public static final String ERROR_CODE_FREQUENCY_ACCESS_RESTRICTED = "54003";
    public static final String ERROR_CODE_NOT_SURPORT_TARGET_LANGUAGE = "58001";
    public static final String ERROR_CODE_INSUFFICIENT_FUNDS = "54004";
    public static final String ERROR_CODE_LONG_QUERY_TOO_FREQUENT = "54005";

    public String erro;
    public String error_msg;

    private String from;
    private String to;
    List<Result> trans_result;

    class Result{
        private String src;
        private String dst;

        public String getSrc() {
            return src;
        }

        public String getDst() {
            return dst;
        }
    }

    public String getErro() {
        return erro;
    }

    public String getError_msg() {
        return error_msg;
    }

    public String getResult() {
        if (trans_result != null) {
            StringBuilder result = new StringBuilder();
            result.append("百度翻译");
            for (Result r : trans_result) {
                result.append("\n").append(r.getDst());
            }
            return result.toString();
        } else {
            return "";
        }
    }
}
