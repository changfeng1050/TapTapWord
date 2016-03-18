package com.example.changfeng.taptapword.net;

import com.example.changfeng.taptapword.net.result.BaiduResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by changfeng on 2016/3/17.
 */
public interface BaiduFanyiService {
    @GET("/api/trans/vip/translate")
    Call<BaiduResult> getResult(@Query("q") String query, @Query("from") String from, @Query("to") String to, @Query("appid") String appId, @Query("salt") String salt, @Query("sign") String sign);
}
