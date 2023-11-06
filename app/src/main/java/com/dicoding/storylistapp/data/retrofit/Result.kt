package com.dicoding.storylistapp.data.retrofit

sealed class Result<out R> private constructor(){
    data class secces<out T>(val data: T): Result<T>()
    data class  Error(val  error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}