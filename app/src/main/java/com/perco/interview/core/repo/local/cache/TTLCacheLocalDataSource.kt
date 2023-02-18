package com.perco.interview.core.repo.local.cache

import com.perco.interview.core.utils.ValueMapper
import com.perco.interview.core.repo.local.ExpirableData
import com.perco.interview.core.repo.local.LocalDataSource
import com.perco.interview.core.repo.local.TTLExpirationException
import com.perco.interview.core.store.Store
import com.perco.interview.core.store.getLong
import com.perco.interview.core.store.put
import kotlinx.coroutines.flow.map

class TTLCacheLocalDataSource<T : Any, S>(
    private val localDataSource: LocalDataSource<T, S>,
    private val cachePolicy: CachePolicy<T>,
    private val requestKeyResolver: ValueMapper<T, String>,
    private val timestampStore: Store<String>
) : LocalDataSource<T, ExpirableData<S>> {

    override fun getData(request: T) = localDataSource.getData(request).map {
        resolveResult(request, it)
    }

    private suspend fun resolveResult(request: T, result: Result<S>): Result<ExpirableData<S>> {
        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull()!!)
        }

        val key = requestKeyResolver.map(request)
        val timestamp = timestampStore.getLong(key, 0)

        val ttlMs = cachePolicy.ttlResolver(request)

        if (System.currentTimeMillis() - timestamp > ttlMs) {
            return Result.failure(TTLExpirationException(key))
        }

        val softTtlMs = cachePolicy.softTtlResolver(request)

        if (System.currentTimeMillis() - timestamp > softTtlMs) {
            return Result.success(ExpirableData.stale(result.getOrThrow()))
        }

        return Result.success(ExpirableData.fresh(result.getOrThrow()))
    }

    override suspend fun setData(request: T, data: ExpirableData<S>) {
        timestampStore.put(
            requestKeyResolver.map(request),
            System.currentTimeMillis()
        )
        localDataSource.setData(request, data.data)
    }

    override suspend fun clear() {
        localDataSource.clear()
        timestampStore.clear()
    }
}