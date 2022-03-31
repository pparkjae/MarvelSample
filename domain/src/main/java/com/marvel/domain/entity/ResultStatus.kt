package com.marvel.domain.entity

import java.io.PrintWriter
import java.io.StringWriter

//sealed class ResultStatus<out T> {
//    object Uninitialized : ResultStatus<Nothing>()
//
//    object Loading : ResultStatus<Nothing>()
//
//    data class Success<T>(val data: T) : ResultStatus<T>()
//
//    data class Error(val exception: Throwable) : ResultStatus<Nothing>()
//}

sealed class ResultStatus<T>(val data: T? = null) {
    class Loading<T>(data: T? = null) : ResultStatus<T>(data)

    class Success<T>(data: T?) : ResultStatus<T>(data)

    class Error<T>(val throwable: Throwable?, data: T? = null) : ResultStatus<T>(data)

//    companion object {
//        fun <T> loading(data: T? = null) = Loading(data)
//
//        fun <T> success(data: T?) = Success(data)
//
//        fun <T> error(throwable: Throwable?, data: T? = null) = Error(throwable = throwable, data)
//    }
}