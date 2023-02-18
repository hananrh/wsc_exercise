package com.perco.interview.core.store

import kotlinx.coroutines.flow.*

interface FlowStore<T : Any> : Store<T> {

    fun <S : Any> getAsFlow(key: T, valueType: Store.ValueType<S>): Flow<S?>
    fun keysFlow(): Flow<List<T>>
}

@Suppress("UNCHECKED_CAST")
class FlowStoreImpl<T : Any>(
    private val store: Store<T>
) : FlowStore<T>, Store<T> by store {

    private val changesFlow = MutableSharedFlow<T>()
    private val keysFlow = MutableSharedFlow<List<T>>()

    override fun <S : Any> getAsFlow(key: T, valueType: Store.ValueType<S>) =
        changesFlow
            .onSubscription {
                emit(key)
            }
            .filter {
                it == key
            }
            .map {
                get(key, valueType)
            }.distinctUntilChanged()

    override suspend fun <S : Any> put(key: T, data: S?, valueType: Store.ValueType<S>) {
        store.put(key, data, valueType)
        changesFlow.emit(key)
        keysFlow.emit(keys())
    }

    override suspend fun remove(key: T) {
        store.remove(key)
        changesFlow.emit(key)
        keysFlow.emit(keys())
    }

    override suspend fun clear() {
        val keys = keys()
        store.clear()
        keys.forEach { changesFlow.emit(it) }
        keysFlow.emit(keys())
    }

    override fun keysFlow() = keysFlow.distinctUntilChanged()
}

fun <T : Any> Store<T>.asFlowStore(): FlowStore<T> = FlowStoreImpl(this)

fun <T : Any> FlowStore<T>.getBoolean(key: T) =
    getAsFlow(key, Store.ValueType.Boolean)

fun <T : Any> FlowStore<T>.getBoolean(key: T, default: Boolean) =
    getBoolean(key).map { it ?: default }

fun <T : Any> FlowStore<T>.getInt(key: T) =
    getAsFlow(key, Store.ValueType.Int)

fun <T : Any> FlowStore<T>.getInt(key: T, default: Int) =
    getInt(key).map { it ?: default }

fun <T : Any> FlowStore<T>.getLong(key: T) =
    getAsFlow(key, Store.ValueType.Long)

fun <T : Any> FlowStore<T>.getLong(key: T, default: Long) =
    getLong(key).map { it ?: default }

fun <T : Any> FlowStore<T>.getFloat(key: T) =
    getAsFlow(key, Store.ValueType.Float)

fun <T : Any> FlowStore<T>.getFloat(key: T, default: Float) =
    getFloat(key).map { it ?: default }

fun <T : Any> FlowStore<T>.getString(key: T) =
    getAsFlow(key, Store.ValueType.String)

fun <T : Any> FlowStore<T>.getString(key: T, default: String) =
    getString(key).map { it ?: default }

fun <T : Any> FlowStore<T>.getStringSet(key: T) =
    getAsFlow(key, Store.ValueType.StringSet)

fun <T : Any> FlowStore<T>.getStringSet(key: T, default: Set<String>) =
    getStringSet(key).map { it ?: default }