package com.lyd.llogutil.myLog

import android.content.Context
import androidx.startup.Initializer

import com.lyd.llog.LLog
import com.lyd.llog.interceptor.LinearInterceptor
import com.lyd.llog.interceptor.LogcatInterceptor
import com.lyd.llog.interceptor.PackToLogInterceptor
import com.lyd.llog.interceptor.WriteInInterceptor
import com.lyd.llog.logWrite.FileLogDefaultDiskStrategy
import com.lyd.llog.logWrite.LogDefaultWriter
import com.lyd.llog.logWrite.LogWriteDefaultFormatStrategy
import kotlin.system.measureTimeMillis

class LLogInitializer : Initializer<LLog> {
    override fun create(context: Context): LLog {
        val timeCost = measureTimeMillis {
            LLog.apply {
                setDebug(methodNameEnable = true)
                addInterceptor(LogcatInterceptor())
                addInterceptor(LinearInterceptor())
                addInterceptor(PackToLogInterceptor())
                addInterceptor(
                    WriteInInterceptor(
                        logWriter = LogDefaultWriter(
                            formatStrategy = LogWriteDefaultFormatStrategy(),
                            diskStrategy = FileLogDefaultDiskStrategy(
                                logDirectory = context.getExternalFilesDir("log")!!.absolutePath,
                                logFileStoreSizeOfMB = 2,
                                logFileMaxNumber = 4
                            )
                        )
                    )
                )
            }
        }
        LLog.i(msg = "---------LLog 初始化完成 耗时$timeCost--------------")
        return LLog
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList<Class<Initializer<*>>>()
    }
}