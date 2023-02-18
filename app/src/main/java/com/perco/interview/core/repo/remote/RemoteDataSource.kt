package com.shire.robin.repo.base.remote

interface RemoteDataSource<T : Any, S> {
    suspend fun getData(request: T): Result<S>
}