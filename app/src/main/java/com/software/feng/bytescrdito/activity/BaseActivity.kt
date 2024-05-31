package com.software.feng.bytescrdito.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
abstract class BaseActivity <T : ViewBinding>(val inflater: (LayoutInflater) -> T)
    : AppCompatActivity() {
    lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = inflater(layoutInflater)
        setContentView(mBinding.root)
        init()
    }

    abstract fun init()
}