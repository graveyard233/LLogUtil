package com.lyd.llog.interceptor

import com.lyd.llog.Chain
import com.lyd.llog.Interceptor
import com.lyd.llog.logExt.singleLogDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * An [Interceptor] make log in sequence, free of multi-thread problem
 */
class LinearInterceptor : Interceptor<Any>() {

    private val CHANNEL_CAPACITY = 50
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * A queue to cache log in memory
     */
    private val channel = Channel<Event>(CHANNEL_CAPACITY)

    init {
        scope.launch(singleLogDispatcher) {
            channel.consumeEach { event ->
                try {
                    event.apply { chain.proceed(tag, message, priority, listOf(time)) }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun log(tag: String, message: Any, priority: Int, chain: Chain, args :List<Any>?) {
        if (isLoggable(message)) {
            scope.launch { channel.send(Event(tag, message, priority, args?.get(0)!! as Long, chain)) }
        }
    }


    data class Event(val tag: String, val message: Any, val priority: Int, val time: Long, val chain: Chain)
}