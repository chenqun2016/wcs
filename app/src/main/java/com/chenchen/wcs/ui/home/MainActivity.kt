package com.chenchen.wcs.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chenchen.wcs.BaseActivity
import com.chenchen.wcs.R
import com.chenchen.wcs.bean.StorageInfosBean
import com.chenchen.wcs.constants.Constants.Companion.RC_CAMERA
import com.chenchen.wcs.constants.Constants.Companion.REQUEST_CODE_DETAIL
import com.chenchen.wcs.constants.Constants.Companion.REQUEST_CODE_SCAN
import com.chenchen.wcs.databinding.ActivityMainBinding
import com.chenchen.wcs.ui.EasyCaptureActivity
import com.chenchen.wcs.ui.detail.DetailActivity
import com.chenchen.wcs.utils.LoadmoreUtils
import com.king.zxing.CameraScan
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        viewModel.storageInfo.observe(this, Observer {
            val data = it.getOrNull()
            if(null != data){
                loadmoreUtils?.onSuccess(mAdapter,data.records)
            }else{
                loadmoreUtils?.onFail(mAdapter,"")
            }

        })
        viewModel.getInfos(1,10)
    }

    var loadmoreUtils : LoadmoreUtils? = null
    lateinit var mAdapter : HomeAdapter
    private fun initViews() {
        binding.layoutTitle.ivClose.visibility = View.GONE
        binding.layoutTitle.title.text = "待入库列表"

        mAdapter = HomeAdapter()
        mAdapter.addChildClickViewIds(R.id.tv_6)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tv_6){
                val bean = adapter.data[position] as StorageInfosBean.RecordsBean
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("markId",bean.prodMarkId)
                startActivityForResult(intent,REQUEST_CODE_DETAIL)
            }
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = mAdapter
        binding.tvScan.setOnClickListener {
            checkCameraPermissions()
        }

        loadmoreUtils = object : LoadmoreUtils() {
            override fun getDatas(page: Int) {
                super.getDatas(page)
                viewModel.getInfos(page,LoadmoreUtils.PAGE_SIZE)
            }
        }
        loadmoreUtils?.initLoadmore(mAdapter, binding.srl)
//        loadmoreUtils?.setEmptyView(empty)

//        loadmoreUtils?.refresh(mAdapter)
    }

    class HomeAdapter() : BaseQuickAdapter<StorageInfosBean.RecordsBean, BaseViewHolder>(R.layout.item_home_infos),
        LoadMoreModule {
        override fun convert(holder: BaseViewHolder, item: StorageInfosBean.RecordsBean) {
            val tv_6 = holder.getView<TextView>(R.id.tv_6)
            holder.setText(R.id.tv_1,item.prodName)
            holder.setText(R.id.tv_2,"部品号："+item.prodCode)
            holder.setText(R.id.tv_3,"入库数量："+item.prodCount)
            holder.setText(R.id.tv_4,"生产日期："+item.prodCreateDate)
            holder.setText(R.id.tv_5,"条形码："+item.prodMarkId)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_DETAIL ){
                loadmoreUtils?.refresh(mAdapter)
            }
            if(requestCode == REQUEST_CODE_SCAN ){
                val result = CameraScan.parseScanResult(data)
//                Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("markId",result)
                startActivityForResult(intent,REQUEST_CODE_DETAIL)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, EasyCaptureActivity::class.java)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera),
                RC_CAMERA, *perms
            )
        }
    }
}