package com.perco.interview.core.repo

import kotlinx.coroutines.flow.Flow

interface Repo<T : Any, S> {

    fun getData(request: T): Flow<Result<S>>
}

interface MutableRepo<T : Any, S> : Repo<T, S> {

    suspend fun setData(request: T, data: S?)
}

interface MutableCollectionRepo<T : Any, S, K : Collection<S>> : MutableRepo<T, K> {
    suspend fun add(item: S)
}

interface MutableListRepo<T : Any, S> : MutableCollectionRepo<T, S, List<S>>