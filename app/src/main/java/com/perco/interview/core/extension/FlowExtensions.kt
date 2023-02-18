package com.perco.interview.core.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, S> Flow<Result<T>>.mapSuccess(map: (T) -> S) = map {
    if (it.isFailure) {
        Result.failure(it.exceptionOrNull()!!)
    } else {
        Result.success(map(it.getOrThrow()))
    }
}
