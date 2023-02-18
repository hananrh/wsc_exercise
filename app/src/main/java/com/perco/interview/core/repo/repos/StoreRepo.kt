package com.perco.interview.core.repo.repos

import com.perco.interview.core.repo.MutableRepo
import com.perco.interview.core.repo.local.NoData
import com.perco.interview.core.store.Store
import kotlinx.coroutines.flow.flow

class StoreRepo<T : Any, S : Any>(
    private val store: Store<T>,
    private val dataClass: Class<S>
) : MutableRepo<T, S> {

    private val valueType
        get() = Store.ValueType.Object<S>(dataClass)

    override fun getData(request: T) = flow {
        val data = store.get(request, valueType)
        emit(if (data == null) Result.failure(NoData) else Result.success(data))
    }

    override suspend fun setData(request: T, data: S?) {
        store.put(request, data, valueType)
    }

    companion object {
        inline fun <T : Any, reified S : Any> ofType(store: Store<T>) =
            StoreRepo(store, S::class.java)
    }
}