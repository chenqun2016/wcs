package com.chenchen.wcs.network

import com.chenchen.wcs.network.base.BaseNetworkApi
import com.chenchen.wcs.params.LoginParams
import com.chenchen.wcs.params.StartParams
import com.chenchen.wcs.utils.toApiBody

/**
 * 网络请求具体实现
 */
object NetworkApi : BaseNetworkApi<INetworkService>("http://159.75.76.133/wcs/") {

    suspend fun login(param: LoginParams) = getResult {
        service.login(param.toApiBody())
    }

    suspend fun storageInfos(page:Int,pageSize:Int) = getResult {
        service.storageInfos(page,pageSize)
    }

    suspend fun getByProdMarkId(markId:String) = getResult {
        service.getByProdMarkId(markId)
    }

    suspend fun wareList() = getResult {
        service.wareList()
    }

    suspend fun getRootCellsByWareId(wareId:String) = getResult {
        service.getRootCellsByWareId(wareId)
    }

    suspend fun getChildCells(wareId:String) = getResult {
        service.getChildCells(wareId)
    }

    suspend fun start(param: StartParams) = getResult {
        service.start(param.toApiBody())
    }

    suspend fun checkAndGetCellPathList(cellCode:String) = getResult {
        service.checkAndGetCellPathList(cellCode)
    }
}