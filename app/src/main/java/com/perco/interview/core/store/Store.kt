package com.perco.interview.core.store

import com.squareup.moshi.Types
import java.lang.reflect.Type

interface Store<T> {

    suspend fun <S : Any> get(key: T, valueType: ValueType<S>): S?
    suspend fun <S : Any> put(key: T, data: S?, valueType: ValueType<S>)
    suspend fun remove(key: T)
    suspend fun keys(): List<T>
    suspend fun clear()

    fun clone(name: String): Store<T>

    sealed class ValueType<T> {
        object Long : ValueType<kotlin.Long>()
        object Float : ValueType<kotlin.Float>()
        object Int : ValueType<kotlin.Int>()
        object Boolean : ValueType<kotlin.Boolean>()
        object String : ValueType<kotlin.String>()
        object StringSet : ValueType<Set<kotlin.String>>()
        class Object<T>(val type: Type) : ValueType<T>() {
            companion object {
                inline fun <reified T> of() = Object<T>(T::class.java)
                inline fun <reified T> ofListType() =
                    Object<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
            }
        }
    }
}

interface TransactionStore<T> : Store<T> {
    fun beginTransaction(): Transaction<T>

    /**
     * Performs block() and executes when done
     */
    suspend fun execute(block: suspend Transaction<T>.() -> Unit)
}

interface Transaction<T> {
    suspend fun clear()
    suspend fun remove(key: T)
    suspend fun <S : Any> put(key: T, data: S?, valueType: Store.ValueType<S>)
    suspend fun execute()
}

suspend fun <T : Any> Store<T>.getBoolean(key: T) =
    get(key, Store.ValueType.Boolean)

suspend fun <T : Any> Store<T>.getBoolean(key: T, default: Boolean) =
    get(key, Store.ValueType.Boolean) ?: default

suspend fun <T : Any> Store<T>.getInt(key: T) =
    get(key, Store.ValueType.Int)

suspend fun <T : Any> Store<T>.getInt(key: T, default: Int) =
    getInt(key) ?: default

suspend fun <T : Any> Store<T>.getLong(key: T) =
    get(key, Store.ValueType.Long)

suspend fun <T : Any> Store<T>.getLong(key: T, default: Long) =
    getLong(key) ?: default

suspend fun <T : Any> Store<T>.getFloat(key: T) =
    get(key, Store.ValueType.Float)

suspend fun <T : Any> Store<T>.getFloat(key: T, default: Float) =
    getFloat(key) ?: default

suspend fun <T : Any> Store<T>.getString(key: T) =
    get(key, Store.ValueType.String)

suspend fun <T : Any> Store<T>.getString(key: T, default: String) =
    getString(key) ?: default

suspend fun <T : Any> Store<T>.getStringSet(key: T) =
    get(key, Store.ValueType.StringSet)

suspend fun <T : Any> Store<T>.getStringSet(key: T, default: Set<String>) =
    getStringSet(key) ?: default

suspend fun <T : Any> Store<T>.put(key: T, value: Boolean) =
    put(key, value, Store.ValueType.Boolean)

suspend fun <T : Any> Store<T>.put(key: T, value: Int) =
    put(key, value, Store.ValueType.Int)

suspend fun <T : Any> Store<T>.put(key: T, value: Long) =
    put(key, value, Store.ValueType.Long)

suspend fun <T : Any> Store<T>.put(key: T, value: Float) =
    put(key, value, Store.ValueType.Float)

suspend fun <T : Any> Store<T>.put(key: T, value: Set<String>) =
    put(key, value, Store.ValueType.StringSet)

suspend inline fun <T : Any, reified S> Store<T>.removeFromList(key: T, value: S) {
    val list = (get(key, valueType = Store.ValueType.Object.ofListType<S>())
        ?: emptyList()).toMutableList()
        .apply { remove(value) }
    put(key, list, Store.ValueType.Object.ofListType())
}

suspend inline fun <T : Any, reified S> Store<T>.addToList(
    key: T,
    value: S,
    position: Int? = null
) {
    val list = (get(key, valueType = Store.ValueType.Object.ofListType<S>())
        ?: emptyList()).toMutableList()
        .apply {
            add(position ?: size, value)
        }
    put(key, list, Store.ValueType.Object.ofListType())
}

suspend fun <T : Any> Transaction<T>.put(key: T, value: String) =
    put(key, value, Store.ValueType.String)

suspend fun <T : Any> Transaction<T>.put(key: T, value: Boolean) =
    put(key, value, Store.ValueType.Boolean)

suspend fun <T : Any> Transaction<T>.put(key: T, value: Int) =
    put(key, value, Store.ValueType.Int)

suspend fun <T : Any> Transaction<T>.put(key: T, value: Long) =
    put(key, value, Store.ValueType.Long)

suspend fun <T : Any> Transaction<T>.put(key: T, value: Float) =
    put(key, value, Store.ValueType.Float)

suspend fun <T : Any> Transaction<T>.put(key: T, value: Set<String>) =
    put(key, value, Store.ValueType.StringSet)
