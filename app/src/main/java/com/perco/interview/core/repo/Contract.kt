package com.perco.interview.core.repo

import kotlinx.coroutines.flow.Flow

interface Repo<T : Any, S> {

    fun getData(request: T): Flow<Result<S>>
}