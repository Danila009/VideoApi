package ru.youTube.common

sealed class Result<T>(val message:String? = null, val data:T? = null){
    class Error<T>(message: String):Result<T>(message = message)
    class Success<T>(data: T?):Result<T>(data = data)
}