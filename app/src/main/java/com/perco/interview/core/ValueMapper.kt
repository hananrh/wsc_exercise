package com.perco.interview.core

fun interface ValueMapper<T, S> {
    fun map(value: T): S
}