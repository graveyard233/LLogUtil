package com.lyd.llog.logWrite

/**
 * 日志默认写入类
 * @param formatStrategy 日志格式化策略，有默认策略
 * @param diskStrategy 存储策略
 * */
class LogDefaultWriter(
    private val formatStrategy : BaseFormatStrategy = LogWriteDefaultFormatStrategy(),
    private val diskStrategy : BaseLogDiskStrategy
) : BaseLogWriter() {
    override fun getLogcatFormatStrategy(): BaseFormatStrategy {
        return formatStrategy
    }

    override fun getLogDiskStrategy(): BaseLogDiskStrategy {
        return diskStrategy
    }
}