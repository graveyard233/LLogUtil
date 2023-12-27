package com.lyd.llog.logWrite

import java.text.SimpleDateFormat
import java.util.Locale

abstract class BaseFormatStrategy {

    /**yyyy-MM-dd HH:mm:ss.SSS*/
    protected val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
    /**日志写入格式*/
    abstract fun format(
        logTime :Long,
        tag :String,
        logLevel :Int,
        data :String
    ) :String
}