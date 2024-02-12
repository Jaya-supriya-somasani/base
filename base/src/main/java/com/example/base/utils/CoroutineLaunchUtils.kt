package com.example.base.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
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
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context + coroutineExceptionHandler, start = start, block = block)
}

fun LifecycleOwner.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    showToastOnError: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.safeLaunch(context, start) {
        try {
            block()
        } catch (e: Exception) {
            Log.e("Error", e.toString())
            if (showToastOnError && e !is CancellationException) {
//                showToast(e)
            }
            throw e
        }
    }
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