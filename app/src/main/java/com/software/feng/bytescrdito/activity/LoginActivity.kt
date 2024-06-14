package com.software.feng.bytescrdito.activity

import android.content.Intent
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
import com.software.feng.utillibrary.util.ToastUtil
import com.tencent.mmkv.MMKV

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
        if (mBinding.etNumber.text.toString().isEmpty() ){
            ToastUtil.showToast(this,"Complete el número de teléfono correcto")
            return
        }
        if ( mBinding.etNumber.text.toString().length!=10 || mBinding.etNumber.text.toString().startsWith("0")){
            ToastUtil.showToast(this,"El primer dígito del número de teléfono de 10 dígitos no es 0")
            return
        }
        vmLogin.value.getSMS(mBinding.etNumber.text.toString())
    }


    private fun checkInput() {
        if (mBinding.etNumber.text.toString().isEmpty() ){
            ToastUtil.showToast(this,"Complete el número de teléfono correcto")
            return
        }
        if ( mBinding.etNumber.text.toString().length!=10 || mBinding.etNumber.text.toString().startsWith("0")){
            ToastUtil.showToast(this,"El primer dígito del número de teléfono de 10 dígitos no es 0")
            return
        }
        if (mBinding.etNumber.text.toString().isEmpty()){
            ToastUtil.showToast(this,"llenar OTP")
            return
        }
        if (!mBinding.pcb.isChecked){
            ToastUtil.showToast(this,"Por favor acepte los acuerdos")
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
            val b = MMKV.defaultMMKV().getInt("woshishei",3)
            if (b == 2){
                startActivity(Intent(this,com.software.feng.bytescrdito.adapter4androidx.MainActivity::class.java))
            }
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