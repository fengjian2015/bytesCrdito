package com.software.feng.bytescrdito.activity.vm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software.feng.bytescrdito.activity.WebActivity
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.utillibrary.util.LogUtil

/**
 * Time：2024/5/13
 * Author：feng
 * Description：
 */
class VMWeb: ViewModel() {
    val openWebLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun openWeb(activity: AppCompatActivity,webOpenModel: WebOpenModel){
        val intent = Intent(activity,WebActivity::class.java)
        intent.putExtra("open",webOpenModel)
        activity.startActivity(intent)
        openWebLiveData.postValue(true)
    }

    fun staticLogin(function: Function1<Int,Int>){
        NetRequestManage.checkUpdate(object : Function1<Int,Int>{
            override fun invoke(p1: Int): Int {
                if (p1 == 3){
                    function.invoke(3)
                }
                return 0
            }
        })
    }
}