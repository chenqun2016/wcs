package com.chenchen.wcs.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chenchen.wcs.bean.StorageInfosBean
import com.chenchen.wcs.network.NetworkApi
import kotlinx.coroutines.launch

/**
 * 创建时间：2021/12/21
 * @Author： 陈陈陈
 * 功能描述：
 */
class HomeViewModel : ViewModel(){

    val storageInfo : MutableLiveData<Result<StorageInfosBean?>> = MutableLiveData()
    fun getInfos(page:Int,pageSize:Int){
        viewModelScope.launch {
            val result = NetworkApi.storageInfos(page,pageSize)
            storageInfo.value = result
        }
    }
}