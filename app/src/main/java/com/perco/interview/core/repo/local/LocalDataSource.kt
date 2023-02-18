package com.perco.interview.core.repo.local

import kotlinx.coroutines.flow.Flow

interface LocalDataSource<T : Any, S> {
    fun getData(request: T): Flow<Result<S>>
    suspend fun setData(request: T, data: S)
    suspend fun clear()
}