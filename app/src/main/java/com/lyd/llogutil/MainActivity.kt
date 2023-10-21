package com.lyd.llogutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lyd.llog.LLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LLog.i(msg = "Test my log")
        try {
            val a = 1 / 0
        } catch (ex :Exception){
            LLog.e(msg = ex)
        }
    }
}