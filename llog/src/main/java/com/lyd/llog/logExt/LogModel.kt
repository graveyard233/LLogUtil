package com.lyd.llog.logExt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogItem(val time :Long,val logLevel :Int,val tag :String,val data :String) : Parcelable