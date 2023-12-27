package com.lyd.llog.logWrite

import com.lyd.llog.LLog

/**
 * 日志写入默认格式
 * */
class LogWriteDefaultFormatStrategy : BaseFormatStrategy() {

    companion object{
        private val NEW_LINE = System.getProperty("line.separator")  //换行
    }

    override fun format(
        logTime: Long,
        tag: String,
        logLevel: Int,
        data: String
    ): String {
        val builder = StringBuilder()
        builder.append(
            "${dateFormat.format(logTime)} $tag[${LLog.getLevelByInt(logLevel.toFloat())}] $data"
        ).append(NEW_LINE)
        val logStr = builder.toString()
        builder.clear()
        return logStr
    }
}