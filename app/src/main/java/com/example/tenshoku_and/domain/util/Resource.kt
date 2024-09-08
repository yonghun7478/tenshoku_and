package com.example.tenshoku_and.domain.util

sealed class Resource<out T> {
    object Loading : Resource<Nothing>() // 로딩 상태
    data class Success<out T>(val data: T) : Resource<T>() // 성공 상태 (데이터 포함)
    data class Error(val message: String? = null, val throwable: Throwable? = null) :
        Resource<Nothing>() // 에러 상태 (메시지, 예외 정보 포함)
}