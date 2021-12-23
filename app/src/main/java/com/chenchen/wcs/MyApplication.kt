package com.chenchen.wcs

import android.app.Application
import com.chenchen.wcs.utils.MMKVUtils

/**
 * 创建时间：2021/12/21
 * @Author： 陈陈陈
 * 功能描述：
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MMKVUtils.init(this)
    }
}