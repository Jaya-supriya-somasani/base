package com.example.base.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.lifecycle.whenStateAtLeast
import com.example.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
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

/**
 * Launches and runs the given block when the [Lifecycle] controlling this
 * [LifecycleCoroutineScope] is at least in [Lifecycle.State.RESUMED] state.
 *
 * The returned [Job] will be cancelled when the [Lifecycle] is destroyed.
 * @see Lifecycle.whenResumed
 * @see Lifecycle.coroutineScope
 */
fun LifecycleOwner.safeLaunchWhenResumed(
    showToastOnError: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
) = safeLaunch {
    lifecycle.whenStateAtLeast(Lifecycle.State.RESUMED) {
        try {
            block()
        } catch (e: Exception) {
            if (showToastOnError) {
//                showToast(e)
            }
            throw e
        }
    }
}

suspend inline fun ReceiveChannel<Unit>.collect(crossinline action: suspend () -> Unit) {
    try {
        val iterator = iterator()
        while (iterator.hasNext()) {
            iterator.next()
            action()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}

fun <T> Channel<T>.sendValue(value: T) = runCatching { this.trySend(value) }

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