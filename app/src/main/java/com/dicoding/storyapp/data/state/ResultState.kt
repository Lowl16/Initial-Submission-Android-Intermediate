package com.dicoding.storyapp.data.state

sealed class ResultState<out R> private constructor() {

    data class Success<out T>(val data: T) : ResultState<T>()

    data class Error(val error: String) : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()
}