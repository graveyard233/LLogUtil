package com.lyd.llogutil.myLog

import com.lyd.llog.logWrite.BaseFormatStrategy
import com.lyd.llog.logWrite.BaseLogDiskStrategy
import com.lyd.llog.logWrite.BaseLogWriter


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