package com.example.windy.data.model

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Message(val msg: String) : Response<Nothing>()
}