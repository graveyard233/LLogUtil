package com.lyd.llog.interceptor

import com.lyd.llog.Chain
import com.lyd.llog.Interceptor
import com.lyd.llog.logExt.LogItem

/**
 * 包装的中间层拦截器
 * */
class PackToLogInterceptor : Interceptor<Any>() {
    override fun log(tag: String, message: Any, priority: Int, chain: Chain, args :List<Any>?) {
        if (isLoggable(message) /*&& priority >= android.util.Log.INFO*/){// 只有log等级大于等于logi的才能发射
            // 把每条日志包装成带唯一标识符的 Log
//            val log = Log(UUID.randomUUID().toString(), message)
            val logItem = LogItem(args!![0] as Long,priority,tag,message.toString())
            // 将 Log 沿责任链传递
            chain.proceed(tag, logItem, priority, args)
        }
    }
}