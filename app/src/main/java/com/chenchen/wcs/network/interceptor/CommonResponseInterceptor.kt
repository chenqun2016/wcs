package com.chenchen.wcs.network.interceptor

import com.chenchen.wcs.network.base.BaseNetworkApi
import com.chenchen.wcs.utils.d
import okhttp3.Interceptor
import okhttp3.Response

class CommonResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)
        d(BaseNetworkApi.TAG, "url=${request.url}, requestTime=${System.currentTimeMillis() - startTime}ms")
        return response
    }
}