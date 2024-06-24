package com.software.feng.bytescrdito.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.activity.vm.VMWeb
import com.software.feng.bytescrdito.broad.BatteryReceiver
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.databinding.ActivityWebBinding
import com.software.feng.bytescrdito.http.model.UserInfoResponse
import com.software.feng.bytescrdito.js.JSJavascript
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.bytescrdito.observer.ItemObserver
import com.software.feng.bytescrdito.observer.ObserverManager
import com.software.feng.bytescrdito.observer.ObserverType
import com.software.feng.bytescrdito.util.LocationListenerUtil
import com.software.feng.bytescrdito.util.SignalStrengthUtils
import com.software.feng.bytescrdito.weight.IWebViewClient
import com.software.feng.bytescrdito.weight.UpdateDialog
import com.software.feng.bytescrdito.weight.WebSetting

class WebActivity : BaseActivity<ActivityWebBinding>(ActivityWebBinding::inflate) {
    var jsJavascript: JSJavascript? = null
    val vmWeb  = viewModels<VMWeb>()
    var webOpenModel :WebOpenModel = WebOpenModel()
    val batteryReceiver = BatteryReceiver()

    val itemObserver: ItemObserver =  ItemObserver {
        when(it){
            ObserverType.INIT_LOCATION_LISTENER -> LocationListenerUtil.initLocationListener()
        }
    }

    override fun init() {
        webOpenModel = (intent.getSerializableExtra("open") as WebOpenModel?)!!
        initWebView()
        initViewModel()
        initBroad()
        initDmp()
        ObserverManager.getManager().registerObserver(itemObserver)
        mBinding.ivBack.setOnClickListener {
            back()
        }
    }

    private fun initDmp() {
        SignalStrengthUtils.getPhoneSignalStrength(
            this,
            object : SignalStrengthUtils.SignalStrengthListener{
                override fun onSignalStrengthChanged(signalStrength: Int) {
                    Cons.dbm = signalStrength
                }
            })
    }

    private fun initBroad() {
        if (webOpenModel.isHome) {
            vmWeb.value.staticLogin(object : Function1<Int,Int>{
                override fun invoke(p1: Int): Int {
                    if (p1 == 3 ){
                        val dialog = UpdateDialog(JSUserInfoUtil.getUserInfo())
                        dialog.show(this@WebActivity.supportFragmentManager, "update'")
                    }
                    return 0
                }
            })
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
            registerReceiver(batteryReceiver, intentFilter)
        }
    }

    private fun initViewModel() {
        vmWeb.value.openWebLiveData.observe(this){

        }
    }

    fun initWebView(){
        jsJavascript = JSJavascript(this,mBinding.webView)
        mBinding.webView.addJavascriptInterface(jsJavascript!!,Cons.JavascriptInterfaceName)
        WebSetting.initWeb(mBinding.webView)
        mBinding.webView.webViewClient = IWebViewClient(mBinding.webLoading,mBinding.tvName)

        if (webOpenModel.isHome) {
            mBinding.frame.visibility = View.GONE
        } else {
            mBinding.frame.visibility = View.VISIBLE
        }
//        测试
//        webOpenModel.url = "http://121.41.164.237:9001/m/moxi-jsbridge.html?appName=Credito";
        if (webOpenModel.url != null && !webOpenModel.url.startsWith("http") && !webOpenModel.url.startsWith("file")) {
            webOpenModel.url = "https://"+webOpenModel.url
        }
        mBinding.webView.loadUrl(webOpenModel.url)
    }

    fun back(){
        hideSoftKeyboard(this)
        if (mBinding.webView.canGoBack()){
            mBinding.webView.goBack()
        }else{
            if (webOpenModel.isHome) {
                moveTaskToBack(true)
            } else {
                finish()
            }
        }
    }

    /**
     * 使点击回退按钮不会直接退出整个应用程序而是返回上一个页面
     *
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
            return true
        }
        return super.onKeyDown(keyCode, event) //退出整个应用程序
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getManager().unRegisterObserver(itemObserver)
        if (webOpenModel.isHome) {
            unregisterReceiver(batteryReceiver)
        }
    }

    fun hideSoftKeyboard(context: Context) {
        val focus_view = (context as Activity).currentFocus
        if (focus_view != null) {
            val inputManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(focus_view.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        jsJavascript?.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}