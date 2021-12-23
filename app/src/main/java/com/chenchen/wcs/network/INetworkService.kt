package com.chenchen.wcs.network

import com.chenchen.wcs.bean.CellsBean
import com.chenchen.wcs.bean.LoginBean
import com.chenchen.wcs.bean.StorageInfosBean
import com.chenchen.wcs.bean.WareBean
import com.chenchen.wcs.network.base.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface INetworkService {

    @POST("auth/login")
    suspend fun login(@Body body: RequestBody?): BaseResponse<LoginBean>

    @GET("storageInfos/waitIn")
    suspend fun storageInfos(@Query("page")  page:Int, @Query("pageSize")  pageSize:Int,): BaseResponse<StorageInfosBean>

    @GET("storageInfos/getByProdMarkId/{markId}")
    suspend fun getByProdMarkId(@Path("markId")  markId:String): BaseResponse<StorageInfosBean.RecordsBean>


    @GET("ware/wareList")
    suspend fun wareList(): BaseResponse<List<WareBean>>

    @GET("wareCell/getRootCellsByWareId/{wareId}")
    suspend fun getRootCellsByWareId(@Path("wareId")  wareId:String): BaseResponse<List<CellsBean>>

    @GET("wareCell/getChildCells/{wareId}")
    suspend fun getChildCells(@Path("wareId")  wareId:String): BaseResponse<List<CellsBean>>

    /**
     * 表单提交接口
     */
    @POST("storageInfos/start")
    suspend fun start(@Body body: RequestBody?): BaseResponse<Any>

    /**
     * 库位选择的第二种方式
     */
    @GET("wareCell/checkAndGetCellPathList/{cellCode}")
    suspend fun checkAndGetCellPathList(@Path("cellCode")  cellCode:String): BaseResponse<List<CellsBean>>
}