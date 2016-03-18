package com.example.changfeng.taptapword.net;


import com.example.changfeng.taptapword.Config;
import com.example.changfeng.taptapword.util.MD5Utils;
import com.example.changfeng.taptapword.util.Utils;
import com.example.changfeng.taptapword.net.result.BaiduResult;
import com.example.changfeng.taptapword.net.result.YoudaoResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by changfeng on 2016/3/17.
 */
public class ApiClient {
    private static final String TAG = "ApiClient";
    public static final String BAIDU_BASE_URL = "http://api.fanyi.baidu.com";
    public static final String YOUDAO_BASE_URL = "http://fanyi.youdao.com";

    Retrofit baiduRetrofit = new Retrofit.Builder()
            .baseUrl(BAIDU_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    Retrofit youdaoRetrofit = new Retrofit.Builder()
            .baseUrl(YOUDAO_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    BaiduFanyiService baiduFanyiService = baiduRetrofit.create(BaiduFanyiService.class);

    YoudaoFanyiService youdaoFanyiService = youdaoRetrofit.create(YoudaoFanyiService.class);

    public void getBaiduResult(String query, Callback<BaiduResult> callback) {
        query = query.replaceAll(" ", "%20");
        try {
            String salt = Utils.getToken();
            String sign = MD5Utils.getMd5(Config.baiduTranslateAppId + query + salt + Config.baiduTranslateAppKey);
            Call<BaiduResult> call = baiduFanyiService.getResult(query, "en", "zh", Config.baiduTranslateAppId, salt, sign);
            call.enqueue(callback);
        } catch (Exception e) {

        }
    }

    public void getYoudaoResult(String query, Callback<YoudaoResult> callback) {
        query = query.replaceAll(" ", "%20");
        try {
            Call<YoudaoResult> call = youdaoFanyiService.getResult(Config.youdaoDictKeyfrom, Config.youdaoDictApiKey, "data", "json", "1.1", query);
            call.enqueue(callback);
        } catch (Exception e) {

        }
    }
}

