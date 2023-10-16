package com.lyd.llog.interceptor

import android.util.Log
import com.lyd.llog.Chain
import com.lyd.llog.Interceptor
import com.lyd.llog.LLog

import java.io.PrintWriter
import java.io.StringWriter

//因为 Logcat 拦截器可以接受任何类型的日志，所以被定义为Interceptor<Any>。
//该拦截器对日志做了格式化：如果是日志是 Throwable 类型的，则在当前日志后追加调用栈，否则将其直接转换为 String。

class LogcatInterceptor : Interceptor<Any>(){

    override fun log(tag: String, message: Any, priority: Int, chain: Chain, args :List<Any>?) {
        if (
            isLoggable(message) &&
            (LLog.minPrintPriority <= priority && priority <= LLog.maxPrintPriority) &&
            /*LLog.curPriority != LLog.NONE*/!(LLog.minPrintPriority == LLog.NONE && LLog.maxPrintPriority == LLog.NONE)// 当最大最小的值都是NONE，则不输出logcat
            )
        {
            Log.println(priority,tag,getFormatLog(message, args))
        }
        chain.proceed(tag, message, priority, args)
    }

    private fun getFormatLog(message: Any, vararg args: Any?): String =
        if (message is Throwable){
            getStackTraceString(message)
        } else {
            if (args.isNotEmpty()) message.toString().format(args)
            else message.toString()
        }

    // 获取堆栈信息
    private fun getStackTraceString(t: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw,false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    private fun String.format(args :Array<out Any>) =
        if (args.isEmpty()) this else String.format(this ,*args)

}