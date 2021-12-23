package com.chenchen.wcs.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chenchen.wcs.bean.CellsBean
import com.chenchen.wcs.bean.StorageInfosBean
import com.chenchen.wcs.bean.WareBean
import com.chenchen.wcs.network.NetworkApi
import com.chenchen.wcs.params.StartParams
import kotlinx.coroutines.launch

/**
 * 创建时间：2021/12/21
 * @Author： 陈陈陈
 * 功能描述：
 */
class DetailViewModel : ViewModel(){

    val storageInfo : MutableLiveData<Result<StorageInfosBean.RecordsBean?>> = MutableLiveData()
    fun getByProdMarkId(markId:String){
        viewModelScope.launch {
            val result = NetworkApi.getByProdMarkId(markId)
            storageInfo.value = result
        }
    }

    val wareBeans :MutableLiveData<Result<List<WareBean>?>> = MutableLiveData()
    fun getWareBeans(){
        viewModelScope.launch {
            val result = NetworkApi.wareList()
            wareBeans.value = result
        }
    }

    val checkCellPathData :MutableLiveData<Result<List<CellsBean>?>> = MutableLiveData()
    fun checkCellPath(cellCode:String){
        viewModelScope.launch {
            val result = NetworkApi.checkAndGetCellPathList(cellCode)
            checkCellPathData.value = result
        }
    }

    val startPush :MutableLiveData<Result<Any?>> = MutableLiveData()
    fun startPush(param: StartParams){
        viewModelScope.launch {
            val result = NetworkApi.start(param)
            startPush.value = result
        }
    }
}