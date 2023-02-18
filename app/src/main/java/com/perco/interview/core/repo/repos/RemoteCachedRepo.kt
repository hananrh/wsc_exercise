package com.perco.interview.core.repo.repos

import com.perco.interview.core.repo.Repo
import com.perco.interview.core.repo.local.ExpirableData
import com.perco.interview.core.repo.local.LocalDataSource
import com.perco.interview.core.utils.ValueMapper
import com.shire.robin.repo.base.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class RemoteCachedRepo<T : Any, S, SA>(
    private val name: String,
    private val remoteSource: RemoteDataSource<T, S>,
    private val localSource: LocalDataSource<T, ExpirableData<SA>>,
    private val remoteDataMapper: ValueMapper<S, SA>,
    private val scope: CoroutineScope
) : Repo<T, SA> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getData(request: T) = localSource.getData(request).transformLatest { res ->
        if (res.isFailure) {
            Timber.d("$name repo - failed to get local data for $request: ${res.exceptionOrNull()}")
            val remoteRes = getAndUpdateFromRemote(request)
            if (remoteRes.isFailure) {
                // Only emit from remote if local empty and failed to get from remote
                Timber.d("$name repo - Failed to fetch remote for for $request, emitting error ${remoteRes.exceptionOrNull()}")
                emit(Result.failure(remoteRes.exceptionOrNull()!!))
            }
            return@transformLatest
        }

        Timber.d("$name repo - emitting value for $request")
        emit(res)
    }.onEach { res ->
        if (res.getOrNull()?.maturity == ExpirableData.Maturity.STALE) {
            Timber.d("$name repo - local data stale for $request, refreshing")
            scope.launch {
                getAndUpdateFromRemote(request)
            }
        }
    }.map { res -> res.map { it.data } }

    private suspend fun getAndUpdateFromRemote(request: T): Result<S> {
        val remoteRes = remoteSource.getData(request)
        if (remoteRes.isSuccess) {
            Timber.d("$name repo - Fetched remote for $request, updating local")
            localSource.setData(
                request,
                ExpirableData.fresh(remoteDataMapper.map(remoteRes.getOrThrow()))
            )
        } else {
            Timber.d("$name repo - Failed to fetch from remote for $request: ${remoteRes.exceptionOrNull()}")
        }

        return remoteRes
    }
}