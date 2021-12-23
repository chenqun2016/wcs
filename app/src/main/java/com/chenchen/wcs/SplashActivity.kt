package com.chenchen.wcs

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.chenchen.wcs.constants.Constants
import com.chenchen.wcs.databinding.ActivitySplashBinding
import com.chenchen.wcs.ui.home.MainActivity
import com.chenchen.wcs.ui.login.LoginActivity
import com.chenchen.wcs.utils.MMKVUtils

/**
 * 创建时间：2021/12/20
 * @Author： 陈陈陈
 * 功能描述：
 */
class SplashActivity : BaseActivity(){
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!TextUtils.isEmpty(MMKVUtils.getString(Constants.TOKEN))){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}