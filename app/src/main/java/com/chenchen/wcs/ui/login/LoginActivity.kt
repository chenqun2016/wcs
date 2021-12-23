package com.chenchen.wcs.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.chenchen.wcs.BaseActivity
import com.chenchen.wcs.ui.home.MainActivity
import com.chenchen.wcs.R
import com.chenchen.wcs.constants.Constants
import com.chenchen.wcs.databinding.ActivityLoginBinding
import com.chenchen.wcs.params.LoginParams
import com.chenchen.wcs.utils.MMKVUtils
import com.google.gson.Gson

/**
 * 创建时间：2021/12/20
 * @Author： 陈陈陈
 * 功能描述：
 */
class LoginActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        viewModel.login.observe(this, Observer {
            val bean = it.getOrNull()
            if(null != bean){
                MMKVUtils.putString(Constants.TOKEN,bean.token)
                MMKVUtils.putString(Constants.USER,Gson().toJson(bean.user))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

    }

    private fun initView() {
        binding.edUserPhone.requestFocus()
        binding.edUserPhone.setSelection(binding.edUserPhone.length())
        binding.edUserPhone.setOnClearClickListener {
            binding.edUserPass.editTextView.setText("")
        }

        binding.edUserPhone.addTextChangedListener {
            if(!TextUtils.isEmpty(it) && !TextUtils.isEmpty(binding.edUserPass.editTextView.text)){
                setButtonStatus(true)
            }else{
                setButtonStatus(false)
            }
        }
        binding.edUserPass.editTextView.addTextChangedListener {
            if(!TextUtils.isEmpty(it) && !TextUtils.isEmpty(binding.edUserPhone.text)){
                setButtonStatus(true)
            }else{
                setButtonStatus(false)
            }
        }

        binding.tvAgree.setOnClickListener {
            viewModel.doLogin(LoginParams(username = binding.edUserPhone.text.toString(),password = binding.edUserPass.editTextView.text.toString()))

        }
    }


    private fun setButtonStatus(aBoolean: Boolean) {
        if (aBoolean) {
            binding.tvAgree.isEnabled = true
            binding.tvAgree.setBackgroundResource(R.drawable.btn_gradient_blue_round)
        } else {
            binding.tvAgree.isEnabled = false
            binding.tvAgree.setBackgroundResource(R.drawable.btn_gradient_grey_round)
        }
    }
}