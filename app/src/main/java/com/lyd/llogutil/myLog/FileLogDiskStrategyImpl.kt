package com.lyd.llogutil.myLog

import android.os.Build
import com.lyd.llog.LLog
import com.lyd.llog.logExt.LogItem
import com.lyd.llog.logWrite.BaseLogDiskStrategy
import com.lyd.llogutil.BuildConfig
import java.io.File
import java.io.FilenameFilter
import java.text.SimpleDateFormat
import java.util.Locale

class FileLogDiskStrategyImpl(
    logDirectory :String,
    val logFileStoreSizeOfMB :Int = 5,
    val logFileMaxNumber :Int = 5
): BaseLogDiskStrategy(logDirectory) {

    private val logFileNameDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA)
    private var currentFilePathCache : FilePathCache? = null


    override fun isLogFilePathAvailable(logFilePath: String?, logBody: String): Boolean {
        return currentFilePathCache?.isMatch(logFilePath) == true
    }

    /**判断是否允许生成日志文件*/
    override fun isAllowCreateLogFile(logTime: Long): Boolean {
        checkAndClearLogFile()
        return true// 我这里没有处理存储空间不够的情况，所以直接返回true
    }

    override fun createLogFile(logItem: LogItem): String {
        var fileName = ""
        val currentLogFileCacheSize = currentFilePathCache?.getCurrentSize()
        // 因为在createLogFile之前，先执行过isAllowCreateLogFile方法，已经清除过文件，所以这里可以直接生成新文件
        if (currentLogFileCacheSize != null && currentLogFileCacheSize > logFileStoreSizeOfMB * 1024 * 1024){
            fileName = getFileName(System.currentTimeMillis())
        } else {
            val fileArray = File(logDirPath).listFiles(FilenameFilter { _, name ->
                return@FilenameFilter (name.startsWith(logPrefix) && name.endsWith(logSuffix))
            })

            if (!fileArray.isNullOrEmpty()){
                val fileList = fileArray.sortedBy {
                    it.name
                }
                val lastFile = fileList.last()
                if (lastFile.length() < logFileStoreSizeOfMB * 1024 *1024){
                    fileName = lastFile.name
                }
            }
            if (fileName.isEmpty()){
                fileName = getFileName(System.currentTimeMillis())
            }
        }

        val path = logDirPath + File.separator + fileName
        LLog.d(msg = "create log file = $path")
        val tempFilePath = FilePathCache(logFileStoreSizeOfMB * 1024 * 1024L,path)
        currentFilePathCache = tempFilePath
        return tempFilePath.filePath
    }

    override fun logHeadInfo(): String? {
        return "AppId:${BuildConfig.APPLICATION_ID}" +
                "\nBuildType:${BuildConfig.BUILD_TYPE}" +
                "\n应用版本:${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}" +
                "\n设备品牌:${Build.BRAND}" +
                "\n手机型号:${Build.MODEL}" +
                "\n系统版本:${Build.VERSION.SDK_INT}" +
                "\n\n"
    }

    private fun checkAndClearLogFile() {
        val logDirFile = File(logDirPath)
        if (!logDirFile.exists() || !logDirFile.isDirectory){
            LLog.e(msg = "log dir not exit")
            return
        }
        val logFileArray = logDirFile.listFiles(FilenameFilter { _, name ->
            val nameStr = name.trim()
            if (nameStr.startsWith(logPrefix) && nameStr.endsWith(logSuffix)){
                return@FilenameFilter true
            }
            return@FilenameFilter false
        })
        var logList = logFileArray?.asList()
        if (logList.isNullOrEmpty()){
            return
        }
        logList = logList.sortedBy { it.name }
        val needToDeleteNum = logList.size - (logFileMaxNumber - 1)
        if (needToDeleteNum > 0){// 大于最大文件持有量才会执行删除
            for (j in 0 until needToDeleteNum){
                LLog.d(msg = "超过最大持有量，删除日志文件 ${logList[0].name}")
                logList[0].delete()// 始终删除第一个
            }
        }
    }

    private fun getFileName(time :Long) :String{
        val tempTimeStr = logFileNameDateFormat.format(time)
        return "$logPrefix$tempTimeStr$logSuffix"
    }

    private class FilePathCache(val logFileMaxSize :Long,val filePath :String){
        private val MAX_RESET_COUNT = 50
        private var curResetCount = 0
        private var currentSize = -1L

        private val logFile by lazy {
            File(filePath)
        }

        init {
            if (logFile.exists() && logFile.isFile){
                currentSize = logFile.length()
            } else {
                currentSize = 0
            }
        }

        fun isMatch(logFilePath: String?) :Boolean{
            if (curResetCount > MAX_RESET_COUNT || currentSize < 0){// 每50次，查一次文件大小
                curResetCount = 0
                currentSize = logFile.length()
            }
            curResetCount++
            // 当前文件大小 < 最大限制大小， 表示匹配
            return currentSize < logFileMaxSize && filePath == logFilePath
        }

        fun getCurrentSize() :Long {
            return currentSize
        }
    }
}