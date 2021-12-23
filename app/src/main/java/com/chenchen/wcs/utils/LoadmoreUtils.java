package com.chenchen.wcs.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chenchen.wcs.R;

import java.util.List;

/**
 * 创建时间：2021/7/7
 * 编写人： 陈陈陈
 * 功能描述：加载更多工具
 * 注意：adapter需要 extends BaseQuickAdapter/BaseMultiItemQuickAdapter  implements LoadMoreModule
 */
public class LoadmoreUtils {
    public static final int PAGE_SIZE = 10;
    View emptyView;
    SwipeRefreshLayout swiperefreshlayout;
    PageInfo pageInfo = new PageInfo();

    static class PageInfo {
        int page = 1;

        void nextPage() {
            page++;
        }

        void reset() {
            page = 1;
        }

        boolean isFirstPage() {
            return page == 1;
        }
    }

    public void initLoadmore(BaseQuickAdapter adapter) {
        initLoadmore(adapter,null);
    }
    public void initLoadmore(BaseQuickAdapter adapter,SwipeRefreshLayout swipe) {
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        emptyView = LayoutInflater.from(adapter.getRecyclerView().getContext()).inflate(R.layout.crecyclerview_empty, adapter.getRecyclerView(), false);

        swiperefreshlayout = swipe;
        if(null != swipe){
            swiperefreshlayout.setColorSchemeResources(R.color.colorPrimary,
                    R.color.colorPrimaryDark);
            swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(adapter);
                }
            });
        }
    }
    public void setEmptyViewText(String text){
        if(null != emptyView){
            TextView tv_empty_text = emptyView.findViewById(R.id.tv_empty);
            tv_empty_text.setText(text);
        }
    }
    public View getEmptyView(){
        return emptyView;
    }

    public void startSwipeRefreshView(){
        if(null != swiperefreshlayout){
            if(swiperefreshlayout.isRefreshing()){
                swiperefreshlayout.setRefreshing(false);
            }
            swiperefreshlayout.setRefreshing(true);
        }
    }


    public void refresh(BaseQuickAdapter mAdapter) {
        mAdapter.getLoadMoreModule().setEnableLoadMore(false);
        // 下拉刷新，需要重置页数
        pageInfo.reset();
        request();
    }

    public void reSetPageInfo(){
        pageInfo.reset();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        request();
    }

    /**
     * 请求数据
     */

    private void request() {
        getDatas(pageInfo.page);
    }

    protected void getDatas(int page) {
    }

    public void onFail(BaseQuickAdapter mAdapter,String e) {
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().loadMoreFail();
        closeSwipeRefreshLayout();
    }



    public void onSuccess(BaseQuickAdapter mAdapter,List data) {
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        if (pageInfo.isFirstPage()) {
            //如果是加载的第一页数据，
            mAdapter.setNewInstance(data);
            if(!mAdapter.hasEmptyView()){
                mAdapter.setEmptyView(emptyView);
            }
        } else {
            //不是第一页，则用add
            mAdapter.addData(data);
        }
        if (null != data && data.size() < PAGE_SIZE) {
            //如果不够一页,显示没有更多数据布局
            mAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            mAdapter.getLoadMoreModule().loadMoreComplete();
        }
        // page加一
        pageInfo.nextPage();
        closeSwipeRefreshLayout();
    }

    /**
     * 关闭SwipeRefreshLayout
     */
    private void closeSwipeRefreshLayout() {
        if(null != swiperefreshlayout){
            swiperefreshlayout.setRefreshing(false);
        }
    }
}
