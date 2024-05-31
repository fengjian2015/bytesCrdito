package com.software.feng.bytescrdito.activity

import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.activity.viewModels
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.activity.vm.VMLogin
import com.software.feng.bytescrdito.activity.vm.VMWeb
import com.software.feng.bytescrdito.databinding.ActivityLoginBinding
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.utillibrary.util.LogUtil

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    val vmLogin = viewModels<VMLogin>()
    val vmWeb  = viewModels<VMWeb>()

    var numberTime : String? = null
    var codeTime : String? = null
    override fun init() {
        NetRequestManage.getProtocolUrlv2()
        initViewModel()
        initClick()
        vmLogin.value.changeTextColor(mBinding.ptv.text.toString())
    }

    private fun initClick() {
        mBinding.btCode.setOnClickListener {
            checkPhone()
        }
        mBinding.btOk.setOnClickListener {
            checkInput()
        }

        mBinding.etNumber.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (numberTime == null){
                    numberTime = DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())!!
                }
                if (mBinding.etNumber.text.length == 10){
                    LogUtil.d("手机号埋点：$numberTime 号码："+mBinding.etNumber.text.toString())
                    NetRequestManage.maidian("2",numberTime!!)
                }
            }
        })

        mBinding.etCode.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (codeTime == null){
                    codeTime = DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())!!
                }
                if (mBinding.etCode.text.length == 4){
                    LogUtil.d("短信埋点：$codeTime 号码："+mBinding.etCode.text.toString())
                    NetRequestManage.maidian("3",codeTime!!)
                }
            }
        })
    }

    private fun checkPhone() {
        if (mBinding.etNumber.text.toString().isEmpty() || mBinding.etNumber.text.toString().length!=10){
            Toast.makeText(this,"Fill in your phone number",Toast.LENGTH_SHORT).show()
            return
        }
        vmLogin.value.getSMS(mBinding.etNumber.text.toString())
    }


    private fun checkInput() {
        if (mBinding.etNumber.text.toString().isEmpty() || mBinding.etNumber.text.toString().length!=10){
            Toast.makeText(this,"Fill in your phone number",Toast.LENGTH_SHORT).show()
            return
        }
        if (mBinding.etNumber.text.toString().isEmpty()){
            Toast.makeText(this,"Fill in OTP",Toast.LENGTH_SHORT).show()
            return
        }
        if (!mBinding.pcb.isChecked){
            Toast.makeText(this,"Please check the agreement first",Toast.LENGTH_SHORT).show()
            return
        }
        NetRequestManage.maidian("4",DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())!!)
        vmLogin.value.login(mBinding.etNumber.text.toString(),mBinding.etCode.text.toString())
    }

    private fun initViewModel() {
        vmLogin.value.sendSuccessLiveData.observe(this){
            mBinding.btCode.isClickable = false
            mBinding.btCode.isEnabled = false

        }
        vmLogin.value.openWebLiveData.observe(this){
            vmWeb.value.openWeb(this, webOpenModel = WebOpenModel(false, it))
        }
        vmLogin.value.openMainWebLiveData.observe(this){
            vmWeb.value.openWeb(this, webOpenModel = WebOpenModel(true, it))
            finish()
        }
        vmLogin.value.textColorLiveData.observe(this){
            mBinding.ptv.text = it
            mBinding.ptv.movementMethod = LinkMovementMethod.getInstance()
        }
        vmLogin.value.timeOverLiveData.observe(this){
            mBinding.btCode.text = "Código"
            mBinding.btCode.isClickable = true
            mBinding.btCode.isEnabled = true
        }
        vmLogin.value.timeLiveData.observe(this){
            mBinding.btCode.text = ""+it
        }
    }

}