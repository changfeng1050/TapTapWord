package com.example.changfeng.taptapword.net;


import com.example.changfeng.taptapword.net.result.YoudaoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by changfeng on 2016/3/17.
 */
public interface YoudaoFanyiService {
    @GET("/openapi.do")
    Call<YoudaoResult> getResult(@Query("keyfrom") String keyFrom, @Query("key") String key, @Query("type") String type, @Query("doctype") String docType, @Query("version") String version, @Query("q") String query);
}
