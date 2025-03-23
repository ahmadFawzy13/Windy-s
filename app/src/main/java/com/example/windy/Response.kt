package com.example.windy

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Failure(val error: String) : Response<Nothing>()
    data class SuccessDataBaseOp(val msg:String): Response<Nothing>()
}