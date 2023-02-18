package com.perco.interview.core.store.mem

import com.perco.interview.core.store.Store

class MemStore<T> : Store<T> {
    private val map = mutableMapOf<T, Any>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <S : Any> get(key: T, valueType: Store.ValueType<S>) = map[key] as S?

    override suspend fun <S : Any> put(key: T, data: S?, valueType: Store.ValueType<S>) {
        if (data == null) {
            map.remove(key)
        } else {
            map[key] = data
        }
    }

    override suspend fun remove(key: T) {
        map.remove(key)
    }

    override suspend fun keys() = map.keys.toList()

    override suspend fun clear() {
        map.clear()
    }

    override fun clone(name: String) = MemStore<T>()
}