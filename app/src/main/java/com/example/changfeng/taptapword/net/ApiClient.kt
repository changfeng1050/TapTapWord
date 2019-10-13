package com.example.changfeng.taptapword.net


import android.util.Log
import com.example.changfeng.taptapword.Config
import com.example.changfeng.taptapword.net.result.BaiduResult
import com.example.changfeng.taptapword.net.result.YoudaoResult
import com.example.changfeng.taptapword.util.MD5Utils
import com.example.changfeng.taptapword.util.Utils
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by changfeng on 2016/3/17.
 */
class ApiClient {

    private var baiduRetrofit = Retrofit
            .Builder()
            .baseUrl(BAIDU_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    private var youdaoRetrofit = Retrofit
            .Builder()
            .baseUrl(YOUDAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    private var baiduFanyiService = baiduRetrofit.create(BaiduFanyiService::class.java)

    private var youdaoFanyiService = youdaoRetrofit.create(YoudaoFanyiService::class.java)

    fun getBaiduResult(query: String, callback: Callback<BaiduResult>) {
        try {
            val salt = Utils.getToken()
            val sign = MD5Utils.getMd5(Config.baiduTranslateAppId + query + salt + Config.baiduTranslateAppKey)
            val call = baiduFanyiService.getResult(query, "en", "zh", Config.baiduTranslateAppId, salt, sign)
//            Log.i(TAG, "getBaiduResult() " + call.request().url())
            call.enqueue(callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getYoudaoResult(query: String, callback: Callback<YoudaoResult>) {
        try {
            val call = youdaoFanyiService.getResult(Config.youdaoDictKeyfrom, Config.youdaoDictApiKey, "data", "json", "1.1", query)
            Log.i(TAG, "getYoudaoResult() " + call.request().url())
            call.enqueue(callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private const val TAG = "ApiClient"
        const val BAIDU_BASE_URL = "http://api.fanyi.baidu.com"
        const val YOUDAO_BASE_URL = "http://fanyi.youdao.com"
    }
}

