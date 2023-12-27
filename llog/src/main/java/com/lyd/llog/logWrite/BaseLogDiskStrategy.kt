package com.lyd.llog.logWrite

import com.lyd.llog.LLog
import com.lyd.llog.logExt.LogItem
import java.io.File

// https://github.com/elvishew/xLog/blob/master/xlog/src/main/java/com/elvishew/xlog/printer/file/FilePrinter.java
// https://github.com/oi-october/OTLogger/blob/master/otLogger/src/main/java/com/october/lib/logger/disk/BaseLogDiskStrategy.kt
// https://github.com/orhanobut/logger/blob/master/logger/src/main/java/com/orhanobut/logger/DiskLogStrategy.java
abstract class BaseLogDiskStrategy(
    val logDirPath :String
) {

    /**日志前缀*/
    open var logPrefix = "Log_"
    /**日志后缀*/
    open var logSuffix = ".txt"

    private var currentLogFilePath :String? = null

    /**这个是暴露出去获取日志文件路径的方法*/
    internal fun internalGetLogWritePath(logItem: LogItem) :String{
        val logDirFile = File(logDirPath)
        if (!logDirFile.exists() || !logDirFile.isDirectory){
            logDirFile.mkdirs() // 一定能成功，因为是操作应用专属的文件夹，不会有错误，且一定是能读能写，不会有权限问题
        }
        return getLogWritePath(logItem)
    }

    open fun getLogWritePath(logItem: LogItem):String{
        val path = getCurrentLogFilePath()
        if (isLogFilePathAvailable(path,logItem.data)){
            return path!!// 这里一定可以获取到路径，因为是null的话会返回false进不来，路径对不上也进不来，只有创建了路径缓存才有可能进得来
        } else {
            if (!isAllowCreateLogFile(logItem.time)){
                LLog.e(msg = "is not allow create log file")// 这里基本进不来
                return ""
            }
            val tempPath :String = createLogFile(logItem)
            appendLogHeadToNewFile(logHeadInfo(),tempPath)

            setCurrentFilePath(tempPath)
            return getCurrentLogFilePath()!!
        }
    }

    private fun appendLogHeadToNewFile(logHead: String?, filePath: String) {
        if (filePath.isNotEmpty() && !logHead.isNullOrEmpty()){
            val file = File(filePath)
            if (!file.exists() || !file.isFile){
                if (file.createNewFile()){
                    file.appendText(logHead)// 写入文件头
                }
            }
        }
    }


    /**判断日志是否可以输出到文件中*/
    abstract fun isLogFilePathAvailable(logFilePath :String?, logBody :String) :Boolean
    /**判断是否允许生成日志文件*/
    abstract fun isAllowCreateLogFile(logTime :Long) :Boolean
    /**创建日志文件*/
    abstract fun createLogFile(logItem: LogItem): String
    /**日志头信息*/
    abstract fun logHeadInfo() :String?

    /**
     * 获取当前正在被写入的日志文件路径
     * @return 正在被写入的日志路径
     */
    fun getCurrentLogFilePath():String?{
        return currentLogFilePath
    }

    private fun setCurrentFilePath(path:String?){
        currentLogFilePath = path
    }
}