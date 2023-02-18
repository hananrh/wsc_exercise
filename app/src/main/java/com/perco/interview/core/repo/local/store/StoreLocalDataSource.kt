package com.perco.interview.core.repo.local.store

import com.perco.interview.core.repo.local.LocalDataSource
import com.perco.interview.core.repo.local.NoData
import com.perco.interview.core.store.FlowStore
import com.perco.interview.core.store.Store
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type

class StoreLocalDataSource<T : Any, V : Any, S : Any>(
    private val flowStore: FlowStore<V>,
    private val storeKeyResolver: (T) -> V,
    private val dataType: Type
) : LocalDataSource<T, S> {

    override fun getData(request: T) =
        flowStore.getAsFlow<S>(storeKeyResolver(request), Store.ValueType.Object(dataType))
            .map { value ->
                value?.let { Result.success(value) } ?: Result.failure(NoData)
            }

    override suspend fun setData(request: T, data: S) {
        flowStore.put(storeKeyResolver(request), data, Store.ValueType.Object(dataType))
    }

    override suspend fun clear() {
        flowStore.clear()
    }
}