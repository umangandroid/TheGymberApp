package com.umang.thegymberapp.data.models

import java.lang.Exception

/**
 * Store API/DB operation results to pass to ViewModel
 *
 * @param T
 */
sealed class DataResult<out T> {
    class Success<out T>(val data :T) : DataResult<T>()
    class Error(val exception: Exception) : DataResult<Nothing>()
}