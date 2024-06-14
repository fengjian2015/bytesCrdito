package com.software.feng.utillibrary.util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.software.feng.utillibrary.R


/**
 * Time：2024/6/13
 * Author：feng
 * Description：
 */
object ToastUtil {


    fun showToast(context: Activity,text : String){
        val inflater: LayoutInflater = context.layoutInflater
        val customToastRoot: View = inflater.inflate(R.layout.layout_toast, null)
        val textView: TextView = customToastRoot.findViewById(R.id.text) as TextView
        textView.text = text
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customToastRoot
        toast.show()
    }


}