package com.chenchen.wcs.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chenchen.wcs.BaseActivity
import com.chenchen.wcs.R
import com.chenchen.wcs.bean.CellsBean
import com.chenchen.wcs.bean.StorageInfosBean
import com.chenchen.wcs.bean.WareBean
import com.chenchen.wcs.constants.Constants.Companion.REQUEST_CODE_DETAIL
import com.chenchen.wcs.databinding.ActivityDetailBinding
import com.chenchen.wcs.params.StartParams
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 创建时间：2021/12/21
 * @Author： 陈陈陈
 * 功能描述：
 */
class DetailActivity :BaseActivity() {
    companion object{
        fun newInstance(activity: Activity,markId:String?){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("markId",markId)
            activity.startActivityForResult(intent,REQUEST_CODE_DETAIL)
        }
    }
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    lateinit var markId :String

    var currentData : StorageInfosBean.RecordsBean? = null

    var currentWare : WareBean? = null

    var currentCell : CellsBean? = null

    var cellsDialog: CellsDialog? = null

    private  var  bottomSheetDialog:BottomSheetDialog? = null
    private  var mAdapter :WareAdapter? = null

    private val handler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 999 && msg.obj.toString() == binding.tv4.text.toString() && binding.tv4.isFocused){
                viewModel.checkCellPath(binding.tv4.text.toString())
            }
            if(msg.what == 998 && msg.obj.toString() == binding.tv1.text.toString() && binding.tv1.isFocused){
                viewModel.getByProdMarkId(binding.tv1.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        markId = intent.getStringExtra("markId").toString()

        initViews()
        initDatas()
    }

    private fun initDatas() {
        viewModel.storageInfo.observe(this, Observer {
            currentData = it.getOrNull()
            if(null != currentData){
                binding.tv20.text = currentData?.prodName
                binding.tv21.text = currentData?.prodCode
                binding.tv22.text = currentData?.prodTypeName
                binding.tv23.text = "${currentData?.prodCount}"
                binding.tv24.text = currentData?.prodUnit
                binding.tv25.text = currentData?.prodScalars
                binding.tv26.text = currentData?.prodCreateDate
                binding.tv27.text = currentData?.custName

            }else{
                binding.tv20.text = ""
                binding.tv21.text = ""
                binding.tv22.text = ""
                binding.tv23.text = ""
                binding.tv24.text = ""
                binding.tv25.text = ""
                binding.tv26.text = ""
                binding.tv27.text = ""
            }
        })

        if(!TextUtils.isEmpty(markId)){
            viewModel.getByProdMarkId(markId+"")
        }

        viewModel.wareBeans.observe(this, Observer {
            val data = it.getOrNull()
            if(null != data && null != mAdapter){
                mAdapter?.setNewInstance(data.toMutableList())
            }
        })

        viewModel.startPush.observe(this, Observer {
            if(it.isSuccess){
                setResult(RESULT_OK)
                Toast.makeText(this,"入库成功",Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        viewModel.checkCellPathData.observe(this, Observer {
            val data = it.getOrNull()
            if(null != data && data.isNotEmpty()){
                currentCell = data[data.size-1]
                binding.tv3.text = ""
                for (bean in data){
                    binding.tv3.text = "${binding.tv3.text} ${bean.cellName}"

                }
            }
        })
    }

    private fun initViews() {
        binding.layoutTitle.ivClose.setOnClickListener { finish() }
        binding.layoutTitle.title.text = "入库产品信息"

        binding.tv1.setText(markId)
        binding.tv1.addTextChangedListener {
            if(binding.tv1.isFocused){
                val obtainMessage = handler.obtainMessage()
                obtainMessage.what = 998
                obtainMessage.obj = it.toString()
                handler.sendMessageDelayed(obtainMessage,1500)
            }
        }

        binding.tv2.setOnClickListener {
            binding.tv4.clearFocus()
            showTraceDialog()
        }

        binding.tv3.setOnClickListener {
            binding.tv4.clearFocus()
            showCellsDialog()
        }

        binding.tv4.addTextChangedListener {
            if(binding.tv4.isFocused){
                val obtainMessage = handler.obtainMessage()
                obtainMessage.what = 999
                obtainMessage.obj = it.toString()
                handler.sendMessageDelayed(obtainMessage,1500)
            }
        }

        binding.tvCommit.setOnClickListener {
            if(null == currentData){
                return@setOnClickListener
            }
            if(null == currentWare){
                Toast.makeText(this,"请先选择仓库",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(null == currentCell){
                Toast.makeText(this,"请选择库位",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val params = StartParams(currentData?.cellStatus,currentData?.id,currentData?.prodCode,
                "${currentData?.prodCount}", currentData?.prodCreateDate,currentData?.prodId,
                currentData?.prodMarkId,currentData?.prodName,currentData?.prodNum,currentData?.prodScalars,
                "${currentData?.prodType}",currentData?.prodUnit,currentCell?.id)
            viewModel.startPush(params)
        }

    }

    private fun showCellsDialog() {
        if(null == currentWare){
            Toast.makeText(this,"请先选择仓库",Toast.LENGTH_SHORT).show()
            return
        }

        if(null == cellsDialog){
            cellsDialog = CellsDialog(this)
            cellsDialog?.setOnCellSelectedListener(object :CellsDialog.OnCellSelectedListener{
                override fun onCellSelected(beans: List<CellsBean>) {
                    if(beans.isNotEmpty()){
                        currentCell = beans[beans.size-1]
                        binding.tv4.setText(currentCell?.cellCode)

                        val stringBuilder = StringBuilder()
                        beans.forEach {
                            stringBuilder.append("${it.cellName} ")
                        }
                        binding.tv3.text = stringBuilder.toString()
                    }

                }
            })
        }
        cellsDialog?.show()
        cellsDialog?.setWareId(currentWare?.id)
    }


    private fun showTraceDialog() {
        if(null == bottomSheetDialog){
            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog?.setContentView(R.layout.dialog_bottom4)
            bottomSheetDialog?.findViewById<View>(R.id.close)!!.setOnClickListener {
                if (null != bottomSheetDialog && bottomSheetDialog!!.isShowing) {
                    bottomSheetDialog?.dismiss()
                }
            }
            bottomSheetDialog?.findViewById<TextView>(R.id.tv_title)?.text = "选择仓库"
            val recyclerview = bottomSheetDialog?.findViewById<RecyclerView>(R.id.recyclerview)
            recyclerview?.layoutManager = LinearLayoutManager(this)
            mAdapter = WareAdapter()
            mAdapter?.setOnItemClickListener { adapter, _, position ->
                if(null != adapter){
                    resetAdapterItem(mAdapter!!,position)
                }
                val ware = adapter.data[position] as WareBean?
                if(null == currentWare || ware?.id != currentWare?.id){
                    currentWare = ware
                    binding.tv2.text = currentWare?.wareName
                    currentCell = null
                    binding.tv3.text = ""
                    binding.tv4.setText("")
                    cellsDialog?.clearDatas()
                }

                if (null != bottomSheetDialog && bottomSheetDialog!!.isShowing) {
                    bottomSheetDialog?.dismiss()
                }
            }
            recyclerview?.adapter = mAdapter
            bottomSheetDialog?.setCanceledOnTouchOutside(true)

        }

        try {
            bottomSheetDialog?.window!!.findViewById<View>(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bottomSheetDialog?.show()
        viewModel.getWareBeans()
    }

    fun resetAdapterItem(adapter:WareAdapter,position:Int){
        val pre = adapter.currentPosition
        adapter.currentPosition = position
        if(pre >= 0){
            adapter.notifyItemChanged(pre)
        }
        adapter.notifyItemChanged(position)
    }

    class WareAdapter() : BaseQuickAdapter<WareBean, BaseViewHolder>(R.layout.item_activity_detail_ware) {
        var currentPosition = -1

        override fun convert(holder: BaseViewHolder, item: WareBean) {
            val text = holder.getView<TextView>(R.id.tv_text)
            val layoutParams = text.layoutParams as LinearLayoutCompat.LayoutParams
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            text.layoutParams = layoutParams
            text.text = item.wareName
            if(currentPosition == getItemPosition(item)){
                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.colorPrimary))
                text.typeface = Typeface.DEFAULT_BOLD
            }else{
                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.color_3B3838))
                text.typeface = Typeface.DEFAULT
            }
        }
    }
}