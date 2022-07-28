package com.kiss.tabletennisscore.common

sealed class Result <out T> {
    class Loading <T>: Result<T>()
    class Success <out T> (val data: T): Result<T>()
    class Empty <T>: Result<T>()
    class Error <out T> (val error: T): Result<T>()
}
