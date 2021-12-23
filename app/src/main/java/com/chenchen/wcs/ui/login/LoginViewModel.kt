package com.chenchen.wcs.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chenchen.wcs.bean.LoginBean
import com.chenchen.wcs.network.NetworkApi
import com.chenchen.wcs.params.LoginParams
import kotlinx.coroutines.launch

/**
 * 创建时间：2021/12/20
 * @Author： 陈陈陈
 * 功能描述：
 */
class LoginViewModel : ViewModel(){

    val login :MutableLiveData<Result<LoginBean?>> = MutableLiveData()
    fun doLogin(param: LoginParams){
            viewModelScope.launch {
                val result = NetworkApi.login(param)
                login.value = result
            }
    }
}