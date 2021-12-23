package com.chenchen.wcs.network.interceptor

import android.os.Build
import com.chenchen.wcs.constants.Constants
import com.chenchen.wcs.utils.MMKVUtils
import okhttp3.Interceptor
import okhttp3.Response

class CommonRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        // 这里添加公共请求头
        builder.addHeader("brand", Build.BRAND)
        builder.addHeader("model", Build.MODEL)
        builder.addHeader("Authorization", MMKVUtils.getString(Constants.TOKEN,""))

        return chain.proceed(builder.build())
    }
}