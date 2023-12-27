package com.lyd.llogutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lyd.llog.LLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 未指定tag
        LLog.i(msg = "Test my log")
        // 指定tag
        LLog.d("Test_TAG", "I specified the tag.")
        try {
            val a = 1 / 0
        } catch (ex :Exception){
            LLog.e(msg = ex)
        }
    }
}