package com.lyd.llog.interceptor

import com.lyd.llog.logWrite.BaseLogWriter
import com.lyd.llog.Chain
import com.lyd.llog.Interceptor
import com.lyd.llog.LLog
import com.lyd.llog.logExt.LogItem


/**
 * 日志写入拦截器，等级处于[LLog.minWritePriority]~[LLog.maxWritePriority]的日志写入本地
 * @param logWriter 日志写入类，依赖它来写入本地
 * */
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