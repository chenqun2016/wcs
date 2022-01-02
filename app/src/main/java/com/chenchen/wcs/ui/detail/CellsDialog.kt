package com.chenchen.wcs.ui.detail

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chenchen.wcs.R
import com.chenchen.wcs.bean.CellsBean
import com.chenchen.wcs.databinding.DialogCellsBinding
import com.chenchen.wcs.network.NetworkApi
import com.chenchen.wcs.ui.base.BaseDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 创建时间：2021/12/22
 * @Author： 陈陈陈
 * 功能描述：
 */
class CellsDialog(context: Activity) : BaseDialog(context) {
    private var mListener: OnCellSelectedListener? = null
    private  var wareId: String? = ""
    private lateinit var  binding :DialogCellsBinding

    lateinit var adapter1 : CellsAdapter
    lateinit var adapter2 : CellsAdapter
    lateinit var adapter3 : CellsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCellsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        try {
//            window!!.findViewById<View>(R.id.design_bottom_sheet)
//                .setBackgroundResource(android.R.color.transparent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        setCanceledOnTouchOutside(true)
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.tvTitle.text = "选择库位"

        adapter1 = CellsAdapter()
        adapter2 = CellsAdapter()
        adapter3 = CellsAdapter()

        binding.recyclerview1.layoutManager = LinearLayoutManager(context)
        binding.recyclerview1.adapter = adapter1

        binding.recyclerview2.layoutManager = LinearLayoutManager(context)
        binding.recyclerview2.adapter = adapter2

        binding.recyclerview3.layoutManager = LinearLayoutManager(context)
        binding.recyclerview3.adapter = adapter3

        adapter1.setOnItemClickListener { adapter, view, position ->
            resetAdapterItem(adapter1,position)
            val bean = adapter.data[position] as CellsBean
            if (!bean.isLeaf){
                getItemDatas(bean.id,1)
            }else{
                val beans = listOf<CellsBean>(bean)
                mListener?.onCellSelected(beans)
                dismiss()
            }
        }

        adapter2.setOnItemClickListener { adapter, view, position ->
            resetAdapterItem(adapter2,position)

            val bean = adapter.data[position] as CellsBean
            if (!bean.isLeaf){
                getItemDatas(bean.id,2)
            }else{
                val preBean = adapter1.data[adapter1.currentPosition]
                val beans = listOf<CellsBean>(preBean,bean)
                mListener?.onCellSelected(beans)
                dismiss()
            }
        }

        adapter3.setOnItemClickListener { adapter, view, position ->
            resetAdapterItem(adapter3,position)
            val bean = adapter.data[position] as CellsBean
            val preBean = adapter1.data[adapter1.currentPosition]
            val preBean2 = adapter2.data[adapter2.currentPosition]
            val beans = listOf<CellsBean>(preBean,preBean2,bean)
            mListener?.onCellSelected(beans)
            dismiss()
        }
    }
    fun resetAdapterItem(adapter:CellsAdapter,position:Int){
        val pre = adapter.currentPosition
        adapter.currentPosition = position
        if(pre >= 0){
            adapter.notifyItemChanged(pre)
        }
        adapter.notifyItemChanged(position)
    }

    fun setWareId(id:String?){
        this.wareId = id
        getDatas()
    }

    private fun getDatas() {
        if(TextUtils.isEmpty(wareId)){
            return
        }
        GlobalScope.launch (Dispatchers.Main){
            val result = NetworkApi.getRootCellsByWareId(wareId+"")
            val data1 = result.getOrNull()
            if(null != data1 && null != adapter1){
                adapter1.setNewInstance(data1.toMutableList())
            }
        }
    }
    private fun getItemDatas(id:String,index:Int) {
        if(TextUtils.isEmpty(id)){
            return
        }
        GlobalScope.launch (Dispatchers.Main){
            val result = NetworkApi.getChildCells(id+"")
            val data1 = result.getOrNull()
            if(null != data1){
                if(index == 1){
                    adapter2.currentPosition = -1
                    adapter2.setNewInstance(data1.toMutableList())

                    adapter3.currentPosition = -1
                    adapter3.setNewInstance(mutableListOf())
                }else if(index == 2){
                    adapter3.currentPosition = -1
                    adapter3.setNewInstance(data1.toMutableList())
                }
            }
        }
    }


    class CellsAdapter() : BaseQuickAdapter<CellsBean, BaseViewHolder>(R.layout.item_activity_detail_ware) {
        var currentPosition = -1

        override fun convert(holder: BaseViewHolder, item: CellsBean) {
            val text = holder.getView<TextView>(R.id.tv_text)
            text.text = item.cellName
//            if(!item.isLeaf){
//                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.color_3B3838))
//            }else{
//                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.color_7C7877))
//            }
            if(currentPosition == getItemPosition(item)){
                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.colorPrimary))
                text.typeface = Typeface.DEFAULT_BOLD
            }else{
                text.setTextColor(AppCompatResources.getColorStateList(text.context,R.color.color_3B3838))
                text.typeface = Typeface.DEFAULT
            }

        }
    }
    fun setOnCellSelectedListener(listener:OnCellSelectedListener){
        this.mListener = listener
    }

    fun clearDatas() {
        adapter1.currentPosition = -1
        adapter2.currentPosition = -1
        adapter3.currentPosition = -1

        adapter1.setNewInstance(mutableListOf())
        adapter2.setNewInstance(mutableListOf())
        adapter3.setNewInstance(mutableListOf())
    }

    interface OnCellSelectedListener{
        fun onCellSelected(beans: List<CellsBean>)
    }
}