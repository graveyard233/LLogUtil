package com.lyd.llog.interceptor

import com.lyd.llog.logWrite.BaseLogWriter
import com.lyd.llog.Chain
import com.lyd.llog.Interceptor
import com.lyd.llog.LLog
import com.lyd.llog.logExt.LogItem


// 以后这个只接收某些类型 只写入某些类型的信息
class WriteInInterceptor(
    private val logWriter: BaseLogWriter
) : Interceptor<LogItem>() {
    override fun log(tag: String, log: LogItem, priority: Int, chain: Chain, args :List<Any>?) {
        if (
            isLoggable(log) && /*priority >= android.util.Log.INFO*/
            (LLog.minWritePriority <= priority && priority <= LLog.maxWritePriority) &&
            !(LLog.minWritePriority == LLog.NONE && LLog.maxWritePriority == LLog.NONE)
            )
        {
            // 将日志写入本地文件
            logWriter.writeIn(log)
        }
    }
}