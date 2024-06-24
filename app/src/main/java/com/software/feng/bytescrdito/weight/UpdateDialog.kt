package com.software.feng.bytescrdito.weight

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.software.feng.bytescrdito.R
import com.software.feng.bytescrdito.http.model.UserInfoResponse
import com.software.feng.bytescrdito.util.ActivityManager

class UpdateDialog constructor(updateBean : UserInfoResponse?): DialogFragment() {
    private var updateBean : UserInfoResponse?
    private lateinit var tvContent:TextView
    private lateinit var Later:ImageView
    init {
        this.updateBean =updateBean
    }
    private lateinit var buttonUpload:Button
    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.layout_update_dialog, null)
        tvContent =rootView.findViewById(R.id.tv_content)
        buttonUpload =rootView.findViewById(R.id.buttonUpload)
        Later = rootView.findViewById(R.id.ivBack)
        Later.setOnClickListener {
            dismiss()
        }
        initData()
        return rootView
    }

    private fun initData(){
        isCancelable = updateBean?.mustUpdate != "1"
        initLaterButton()
        buttonUpload.setOnClickListener {
//            val intent = Intent()
//            intent.action = "android.intent.action.VIEW" //Intent.ACTION_VIEW
//            val content_url = Uri.parse(updateBean.appInstallUrl)
//            intent.data = content_url
//            ActivityManager.getCurrentActivity()?.startActivity(intent)

            try {
                var url = updateBean?.appInstallUrl
                if (url != null && !url.startsWith("http") && !url.startsWith("file")) {
                    url = "https://$url"
                }
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                ActivityManager.getCurrentActivity()?.startActivity(intent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    fun initLaterButton(){
        if (!isCancelable){
            Later.visibility = View.GONE
        }else{
            Later.visibility = View.VISIBLE
        }
    }

}