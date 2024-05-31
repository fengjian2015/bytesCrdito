package com.software.feng.bytescrdito.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.activity.vm.VMWeb
import com.software.feng.bytescrdito.databinding.ActivityPermissionsBinding
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.bytescrdito.util.permissions.PermissionsUtil

class PermissionsActivity : BaseActivity<ActivityPermissionsBinding>(ActivityPermissionsBinding::inflate) {
    val vmWeb  = viewModels<VMWeb>()

    override fun init() {
        NetRequestManage.getProtocolUrlv2()
        mBinding.btOk.setOnClickListener {
            PermissionsUtil.openLocService()
            PermissionsUtil.openWifi()
            XXPermissions.with(this) // 申请多个权限
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.READ_CALENDAR)
                .permission(Permission.READ_CALL_LOG)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.READ_SMS)
                .permission(Permission.CAMERA)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            if (JSUserInfoUtil.getToken()==null || JSUserInfoUtil.getToken() == ""){
                                startActivity(Intent(this@PermissionsActivity,LoginActivity::class.java))
                            }else{
                                vmWeb.value.openWeb(this@PermissionsActivity, webOpenModel = WebOpenModel(true, JSUserInfoUtil.getHomeUrl()!!))
                            }
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        if (doNotAskAgain) {
                            XXPermissions.startPermissionActivity(this@PermissionsActivity, permissions)
                        }
                    }
                })
        }

        mBinding.btCancel.setOnClickListener {
            finish()
        }
    }
}