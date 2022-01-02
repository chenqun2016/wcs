package com.chenchen.wcs.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.chenchen.wcs.R;


/**
 * Created by chenqun on 2017/5/19.
 * 底部产出dialog
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Activity context) {
        super(context, R.style.AddressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(provideContentViewId());

//        initViews(this);
        initPosition();
    }

//    protected abstract int provideContentViewId();

//    protected abstract void initViews(BaseDialog dialog);

    private void initPosition() {
        // 获取到窗体
        Window window = getWindow();
        // 获取到窗体的属性
        WindowManager.LayoutParams params = null;
        if (window != null) {
            params = window.getAttributes();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            // 让对话框展示到屏幕的下边
//            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }

}
