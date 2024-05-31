package com.software.feng.bytescrdito.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.AppsFlyerLib
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.activity.BaseActivity
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.activity.vm.VMWeb
import com.software.feng.bytescrdito.broad.BatteryReceiver
import com.software.feng.bytescrdito.databinding.ActivityMainBinding
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.bytescrdito.observer.ItemObserver
import com.software.feng.bytescrdito.observer.ObserverManager
import com.software.feng.bytescrdito.observer.ObserverType.INIT_LOCATION_LISTENER
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.bytescrdito.util.LocationListenerUtil
import com.software.feng.bytescrdito.util.RiskUtil
import com.software.feng.bytescrdito.util.permissions.PermissionsUtil
import com.software.feng.utillibrary.util.LogUtil
import com.tencent.mmkv.MMKV

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){
    val vmWeb  = viewModels<VMWeb>()
    val itemObserver: ItemObserver =  ItemObserver {
        when(it){
            INIT_LOCATION_LISTENER-> LocationListenerUtil.initLocationListener()
        }
    }
    override fun init() {
        LogUtil.d("APPPID:"+ AppsFlyerLib.getInstance().getAppsFlyerUID(MyApplication.application)?:"")
        val b = MMKV.defaultMMKV().getBoolean("firstIn",true)
        if (b){
            NetRequestManage.maidian("1", DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())!!)
        }
        ObserverManager.getManager().registerObserver(itemObserver)
        vmWeb.value.openWebLiveData.observe(this){
            finish()
        }
        if (XXPermissions.isGranted(
                this,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.CAMERA,
                Permission.READ_PHONE_STATE,
                Permission.READ_SMS,
                Permission.READ_CALENDAR)) {
            if (!RiskUtil.isLocServiceEnable() || !PermissionsUtil.isOpenWifi()) {
                startActivity(Intent(this,PermissionsActivity::class.java))
                return
            }
            if (JSUserInfoUtil.getToken()==null ||JSUserInfoUtil.getToken() == ""){
                startActivity(Intent(this,LoginActivity::class.java))
            }else{
                vmWeb.value.openWeb(this, webOpenModel = WebOpenModel(true, JSUserInfoUtil.getHomeUrl()!!))
            }
        } else {
            startActivity(Intent(this,PermissionsActivity::class.java))
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getManager().unRegisterObserver(itemObserver)
    }
}