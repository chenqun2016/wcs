package com.chenchen.wcs.utils

import android.app.Application
import android.os.Parcelable
import com.chenchen.wcs.constants.Constants
import com.tencent.mmkv.MMKV

/**
 * 本类为MMKV的封装类，防止代码入侵
 */
object MMKVUtils {

    fun init(application: Application) {
        MMKV.initialize(application)
    }

    fun putBoolean( key: String, value: Boolean) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getBoolean( key: String, defaultValue: Boolean = false): Boolean {
        return MMKV.defaultMMKV().decodeBool(key, defaultValue)
    }

    fun putString( key: String, value: String) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getString( key: String, defaultValue: String = ""): String {
        return MMKV.defaultMMKV().decodeString(key, defaultValue)!!
    }

    fun putInt( key: String, value: Int) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getInt( key: String, defaultValue: Int = 0): Int {
        return MMKV.defaultMMKV().decodeInt(key, defaultValue)
    }

    fun putFloat( key: String, value: Float) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getFloat( key: String, defaultValue: Float = 0F): Float {
        return MMKV.defaultMMKV().decodeFloat(key, defaultValue)
    }

    fun putLong( key: String, value: Long) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getLong( key: String, defaultValue: Long = 0L): Long {
        return MMKV.defaultMMKV().decodeLong(key, defaultValue)
    }

    fun putDouble( key: String, value: Double) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getDouble( key: String, defaultValue: Double = 0.0): Double {
        return MMKV.defaultMMKV().decodeDouble(key, defaultValue)
    }

    fun putByteArray( key: String, value: ByteArray) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getByteArray(key: String, defaultValue: ByteArray = ByteArray(0)): ByteArray {
        return MMKV.defaultMMKV().decodeBytes(key, defaultValue)!!
    }

    fun putStringSet( key: String, value: Set<String>) {
        MMKV.defaultMMKV().encode(key, value)
    }

    fun getStringSet( key: String, defaultValue: Set<String> = mutableSetOf()): Set<String> {
        return MMKV.defaultMMKV().decodeStringSet(key, defaultValue)!!
    }

    fun putParcelable( key: String, value: Parcelable) {
        MMKV.defaultMMKV().encode(key, value)
    }

    inline fun <reified T : Parcelable> getParcelable( key: String): T? {
        return MMKV.defaultMMKV().decodeParcelable(key, T::class.java)
    }
}