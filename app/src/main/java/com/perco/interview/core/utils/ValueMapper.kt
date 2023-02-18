package com.perco.interview.core.utils

fun interface ValueMapper<T, S> {
    suspend fun map(value: T): S
}