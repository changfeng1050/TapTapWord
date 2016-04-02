package com.example.changfeng.taptapword.net


import com.example.changfeng.taptapword.net.result.YoudaoResult

import java.util.Observable

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by changfeng on 2016/3/17.
 */
interface YoudaoFanyiService {
    @GET("/openapi.do")
    fun getResult(@Query("keyfrom") keyFrom: String, @Query("key") key: String, @Query("type") type: String, @Query("doctype") docType: String, @Query("version") version: String, @Query("q") query: String): Call<YoudaoResult>

}
