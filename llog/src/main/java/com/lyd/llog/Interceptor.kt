package com.lyd.llog

abstract class Interceptor<T> {
    /**
     * 日志处理逻辑
     * @param args 第一个位置一般放日志的打印时间,第二个位置放exception
     * */
    abstract fun log(tag :String, message :T, priority :Int, chain: Chain, args :List<Any>?)
    // 是否启动当前拦截器
    var isLoggable: (T) -> Boolean = { true }
}