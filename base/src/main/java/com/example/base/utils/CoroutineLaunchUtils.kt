package com.example.base.utils

import com.example.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context + coroutineExceptionHandler, start = start, block = block)
}

val applicationScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Main + coroutineExceptionHandler)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ConflatedChannel() = Channel<T>(Channel.CONFLATED)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ConflatedChannel(initialValue: T) = ConflatedChannel<T>().apply {
    trySend(initialValue)
}

fun <T> MutableSharedFlow<T>.sendValue(value: T) = runCatching { this.tryEmit(value) }
suspend fun <T> BaseViewModel.withLoading(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
//    showLoading()
    return try {
        withContext(context) {
            block()
        }
    } finally {
//        hideLoading()
    }
}