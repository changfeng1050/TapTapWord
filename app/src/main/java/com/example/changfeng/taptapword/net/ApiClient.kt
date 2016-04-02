package com.example.changfeng.taptapword.net


import com.example.changfeng.taptapword.Config
import com.example.changfeng.taptapword.util.MD5Utils
import com.example.changfeng.taptapword.util.Utils
import com.example.changfeng.taptapword.net.result.BaiduResult
import com.example.changfeng.taptapword.net.result.YoudaoResult

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by changfeng on 2016/3/17.
 */
class ApiClient {

    internal var baiduRetrofit = Retrofit.Builder().baseUrl(BAIDU_BASE_URL).addConverterFactory(MoshiConverterFactory.create()).build()

    internal var youdaoRetrofit = Retrofit.Builder().baseUrl(YOUDAO_BASE_URL).addConverterFactory(MoshiConverterFactory.create()).build()

    internal var baiduFanyiService = baiduRetrofit.create(BaiduFanyiService::class.java)

    internal var youdaoFanyiService = youdaoRetrofit.create(YoudaoFanyiService::class.java)

    fun getBaiduResult(query: String, callback: Callback<BaiduResult>) {
        var query = query
        query = query.replace(" ".toRegex(), "%20")
        try {
            val salt = Utils.getToken()
            val sign = MD5Utils.getMd5(Config.baiduTranslateAppId + query + salt + Config.baiduTranslateAppKey)
            val call = baiduFanyiService.getResult(query, "en", "zh", Config.baiduTranslateAppId, salt, sign)
            call.enqueue(callback)
        } catch (e: Exception) {

        }

    }

    fun getYoudaoResult(query: String, callback: Callback<YoudaoResult>) {
        var query = query
        query = query.replace(" ".toRegex(), "%20")
        try {
            val call = youdaoFanyiService.getResult(Config.youdaoDictKeyfrom, Config.youdaoDictApiKey, "data", "json", "1.1", query)
            call.enqueue(callback)
        } catch (e: Exception) {

        }

    }

    companion object {
        private val TAG = "ApiClient"
        val BAIDU_BASE_URL = "http://api.fanyi.baidu.com"
        val YOUDAO_BASE_URL = "http://fanyi.youdao.com"
    }
}

