/*
 * Copyright (C) 2018 Jenly Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chenchen.wcs.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chenchen.wcs.R;
import com.king.zxing.CaptureActivity;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class EasyCaptureActivity extends CaptureActivity {


    @Override
    public int getLayoutId() {
        return R.layout.easy_capture_activity;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = findViewById(R.id.title);
        title.setText("扫码入库");
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();
        getCameraScan()
                .setPlayBeep(true)
                .setVibrate(true);
    }

}
