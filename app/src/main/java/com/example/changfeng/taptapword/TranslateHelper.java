package com.example.changfeng.taptapword;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by changfeng on 2016/1/1.
 */
public class TranslateHelper {
    public static final String TAG = "TranslateHelper";

    Gson gson = new Gson();
    private OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
           return  response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String getYoudaoResult(String query) {
        StringBuilder url = new StringBuilder();
        url.append("http://fanyi.youdao.com/openapi.do?keyfrom=")
                .append(Config.youdaoDictKeyfrom)
                .append("&key=")
                .append(Config.youdaoDictApiKey)
                .append("&type=data&doctype=json&version=1.1&q=")
                .append(query);

        try {
            String result = run(url.toString().replace(" ", "%20"));
            Log.d(TAG, "getYoudaoResult() " + result);
            return YoudaoResult.convertDashToUnderLineInPhonetic(result);
        } catch (IOException e) {
            return "";
        }
    }

    public String getBaiduResult(String query) {
        try {
            String salt = Utils.getToken();
            String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + query
                    + "&from=en&to=zh&appid=" + Config.baiduTranslateAppId
                    + "&salt=" + salt
                    + "&sign=" + MD5Utils.getMd5(Config.baiduTranslateAppId + query + salt + Config.baiduTranslateAppKey)
                    .replace(" ", "%20");
            try {
                String result = run(url);
                Log.d(TAG, "getBaiduResult() " + result);
                return run(url);
            } catch (IOException e) {
                return "";
            }
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

}
