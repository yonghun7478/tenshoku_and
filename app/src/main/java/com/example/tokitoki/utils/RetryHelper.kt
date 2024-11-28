package com.example.tokitoki.utils

import kotlinx.coroutines.delay

class RetryHelper(private val maxRetryCount: Int = 3, private val delayMillis: Long = 2000L) {

    suspend fun <T> retry(action: suspend () -> T?): T? {
        var retryCount = 0

        while (retryCount < maxRetryCount) {
            val result = action()
            if (result != null) {
                return result
            }

            retryCount++
            delay(delayMillis)
        }

        return null
    }
}
