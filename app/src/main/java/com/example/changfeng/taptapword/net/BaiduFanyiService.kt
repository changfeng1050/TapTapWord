package com.example.changfeng.taptapword.net

import com.example.changfeng.taptapword.net.result.BaiduResult

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by changfeng on 2016/3/17.
 */
interface BaiduFanyiService {
    @GET("/api/trans/vip/translate")
    fun getResult(@Query("q") query: String, @Query("from") from: String, @Query("to") to: String, @Query("appid") appId: String, @Query("salt") salt: String, @Query("sign") sign: String): Call<BaiduResult>
}
