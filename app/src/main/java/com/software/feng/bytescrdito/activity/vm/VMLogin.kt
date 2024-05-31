package com.software.feng.bytescrdito.activity.vm

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.bytescrdito.util.DateUtil
import com.tencent.mmkv.MMKV

/**
 * Time：2024/5/29
 * Author：feng
 * Description：
 */
class VMLogin : ViewModel() {
    val openWebLiveData : MutableLiveData<String> by lazy { MutableLiveData() }
    val openMainWebLiveData : MutableLiveData<String> by lazy { MutableLiveData() }
    val textColorLiveData : MutableLiveData<SpannableString> by lazy { MutableLiveData() }
    val timeLiveData : MutableLiveData<Long> by lazy { MutableLiveData() }
    val timeOverLiveData : MutableLiveData<Long> by lazy { MutableLiveData() }
    val sendSuccessLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData() }

    private val codeTime: Long = 60
    private var smsSendTime: Long = 0

    val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val currentTime: Long = DateUtil.getServerTimestamp() / 1000
            val time  = smsSendTime - currentTime
            if (time <= 0){
                timeOverLiveData.postValue(0)
                return
            }else{
                timeLiveData.postValue(time)
                sendEmptyMessage(1)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    fun getSMS(phone : String){
        NetRequestManage.getSMS(phone,object : Function1<Int,Int>{
            override fun invoke(p1: Int): Int {
                if (p1 == 0){
                    sendSuccessLiveData.postValue(true)
                    sendCodeSuccess()
                }
                return 1
            }
        })
    }

    fun login(phone: String, code : String){
        NetRequestManage.Login(phone,code,object : Function1<Int,Int>{
            override fun invoke(p1: Int): Int {
                if (p1 == 0){
                    openMainWebLiveData.postValue(JSUserInfoUtil.getHomeUrl())
                }
                return 1
            }
        })
    }

    fun sendCodeSuccess(){
        smsSendTime = DateUtil.getServerTimestamp() / 1000 + codeTime
        handler.sendEmptyMessage(1)
    }

    fun changeTextColor(text : String){
        val spannableString = SpannableString(text)
        tColorTextClick(spannableString,
            28,
            51,
            Color.parseColor("#FF750A"),
            View.OnClickListener { view: View? ->
                openWebLiveData.postValue(MMKV.defaultMMKV().getString("URL1",""))
            })
        tColorTextClick(spannableString,
            53,
            78,
            Color.parseColor("#FF750A"),
            View.OnClickListener { view: View? ->
                openWebLiveData.postValue(MMKV.defaultMMKV().getString("URL2",""))
            })
        textColorLiveData.postValue(spannableString)
    }

    fun tColorTextClick(
        style: SpannableString,
        start: Int,
        end: Int,
        color: Int,
        onClickListener: View.OnClickListener
    ): SpannableString? {
        style.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickListener.onClick(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return style
    }
}