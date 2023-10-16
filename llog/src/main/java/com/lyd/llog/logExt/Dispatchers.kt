package com.lyd.llog.logExt

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val singleLogDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()