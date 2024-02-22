package com.example.base.utils

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.cancellation.CancellationException

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    if (throwable is CancellationException) return@CoroutineExceptionHandler
    throwable.printStackTrace()
    Log.e(throwable.toString(), "coroutineExceptionHandler")
}