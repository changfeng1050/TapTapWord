package com.example.changfeng.taptapword.net.result;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by changfeng on 2016/3/17.
 */
public class YoudaoResult {
    public static final String TAG = "YoudaoResult";

    //    errorCode：
// 　0 ­ 正常
// 　20 ­ 要翻译的文本过长
// 　30 ­ 无法进行有效的翻译
// 　40 ­ 不支持的语言类型
// 　50 ­ 无效的key
// 　60 ­ 无词典结果，仅在获取词典结果生效
    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_TEXT_TOO_LONG = 20;
    public static final int ERROR_CODE_CANNOT_TRANSLATE_PROPER = 30;
    public static final int ERROR_CODE_NOT_NOT_SUPPORTED_LANGUAGE = 40;
    public static final int ERROR_CODE_INVALID_KEY = 50;
    public static final int ERROR_CODE_NO_RESULT_IN_DICT = 60;
//
//    public static String convertDashToUnderLineInPhonetic(String result) {
//        if (result == null) {
//            return "";
//        }
//        return result.replace("\"" + ORIGIN_UK_PHONETIC + "\"", "\"" + NEW_UK_PHONETIC + "\"")
//                .replace("\"" + ORIGIN_US_PHONETIC + "\"", "\"" + NEW_US_PHONETIC + "\"");
//
//    }
//
//    public static final String ORIGIN_UK_PHONETIC = "uk-phonetic";
//    public static final String ORIGIN_US_PHONETIC = "us-phonetic";
//    public static final String NEW_UK_PHONETIC = "uk_phonetic";
//    public static final String NEW_US_PHONETIC = "us_phonetic";

    private int errorCode;
    private String query;
    private List<String> translation;
    private Basic basic;
    private List<Web> web;

    public String getUkPhonetic() {
        if (basic != null && basic.getUsPhonetic() != null) {
            return basic.getUkPhonetic();
        }
        return "";
    }

    public String getUsPhonetic() {
        if (basic != null && basic.getUsPhonetic() != null) {
            return basic.getUsPhonetic();
        }
        return "";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }


    public static class Basic {
        private String phonetic;
        @Json(name = "uk-phonetic")
        private String ukPhonetic;
        @Json(name = "us-phonetic")
        private String usPhonetic;
        private List<String> explains;

        public String getPhonetic() {
            return phonetic;
        }

        public String getUkPhonetic() {
            return ukPhonetic;
        }

        public String getUsPhonetic() {
            return usPhonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            try {
                result.append("UK:[").append(ukPhonetic).append("] ").append("US:[").append(usPhonetic).append(']');
            } catch (NullPointerException e) {

            }
            result.append("\n");
            for (String explain : explains) {
                result.append("\n").append(explain);
            }
            return result.toString();
        }
    }

    public static class Web {
        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public List<String> getValues() {
            return value;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(key).append(" ");
            for (int i = 0; i < value.size(); i++) {
                result.append(value.get(i) + ";");
            }
            return result.toString();
        }
    }

    public String getDictResult() {
        if (errorCode != ERROR_CODE_SUCCESS) {
            return "";
        }
        if (basic != null) {
            return "有道词典\n" + basic.toString();
        } else {
            return "";
        }
    }

    public String getWebInterpretationResult() {
        if (errorCode != ERROR_CODE_SUCCESS) {
            return "";
        }

        if (web != null) {
            StringBuilder result = new StringBuilder();
            result.append("网络释义");
            for (Web w : web) {
                result.append("\n").append(w.toString());
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public String getTranslateResult() {
        if (errorCode != ERROR_CODE_SUCCESS) {
            return "";
        }
        StringBuilder result = new StringBuilder();

        if (translation != null) {
            result.append("有道翻译");
            for (String t : translation) {
                result.append("\n").append(t);
            }
        }
        return result.toString();
    }


    public String getResult() {
        if (errorCode != ERROR_CODE_SUCCESS) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        final String dictResult = getDictResult();
        final String webInterpretationResult = getWebInterpretationResult();
        final String translateResult = getTranslateResult();

        if (!dictResult.isEmpty()) {
            result.append("\n\n").append(dictResult);
        }
        if (!webInterpretationResult.isEmpty()) {
            result.append("\n\n").append(webInterpretationResult);
        }
        if (!translateResult.isEmpty()) {
            result.append("\n\n").append(translateResult);
        }

        return result.toString();
    }

    public String getParsedExplains() {
        try {
            StringBuilder explainString = new StringBuilder();
            for (int i = 0; i < basic.getExplains().size(); i++) {
                if (i != 0) {
                    explainString.append("\n");
                }
                explainString.append(basic.getExplains().get(i) + ";");
            }
            return explainString.toString();
        } catch (NullPointerException e) {
            return "";
        }

    }

    public String getParseWebTranslate() {
        try {
            StringBuilder webTranslate = new StringBuilder();
            for (int i = 0; i < web.size(); i++) {
                if (i != 0) {
                    webTranslate.append("\n");
                }
                webTranslate.append(web.get(i));
            }
            return webTranslate.toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFormatPhones() {
        StringBuilder phone = new StringBuilder();
        if (getUsPhonetic() != null && !getUsPhonetic().isEmpty()) {
            phone.append("美:[" + getUsPhonetic() + "] ");
        }
        if (getUkPhonetic() != null && !getUkPhonetic().isEmpty()) {
            phone.append("英:[" + getUsPhonetic() + "]");
        }
        return phone.toString();
    }
}
