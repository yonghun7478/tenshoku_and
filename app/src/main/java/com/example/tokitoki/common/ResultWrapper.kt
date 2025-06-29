package com.example.tokitoki.common

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val errorType: ErrorType) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()

    sealed class ErrorType {
        data class ServerError(val httpCode: Int, val message: String) : ErrorType()
        data class ExceptionError(val message: String) : ErrorType()
    }
}