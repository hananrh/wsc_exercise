package com.perco.interview.core.store.prefs

import android.content.Context
import android.content.SharedPreferences
import com.perco.interview.core.json.JsonParser
import com.perco.interview.core.store.Store
import com.perco.interview.core.store.Transaction
import com.perco.interview.core.store.TransactionStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class PrefsStore(
    private val name: String,
    private val context: Context,
    private val jsonParser: JsonParser
) : TransactionStore<String> {

    private val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override suspend fun <S : Any> get(key: String, valueType: Store.ValueType<S>) =
        withContext(Dispatchers.IO) {
            if (!prefs.contains(key)) {
                null
            } else when (valueType) {
                is Store.ValueType.Long -> prefs.getLong(key, 0) as S
                is Store.ValueType.Float -> prefs.getFloat(key, 0f) as S
                is Store.ValueType.Int -> prefs.getInt(key, 0) as S
                is Store.ValueType.Boolean -> prefs.getBoolean(key, false) as S
                is Store.ValueType.String -> prefs.getString(key, null) as S
                is Store.ValueType.StringSet -> prefs.getStringSet(key, null) as S
                is Store.ValueType.Object -> {
                    val str = prefs.getString(key, null)
                    if (str == null) {
                        null
                    } else {
                        jsonParser.fromJson<S>(str, valueType.type)
                    }
                }
            }
        }

    override suspend fun <S : Any> put(key: String, data: S?, valueType: Store.ValueType<S>) {
        withContext(Dispatchers.IO) {
            val editor = prefs.edit()
            putInEditor(editor, key, data, valueType)
            editor.commit()
        }
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.IO) {
            prefs.edit().remove(key).commit()
        }
    }

    private fun <S : Any> putInEditor(
        editor: SharedPreferences.Editor,
        key: String,
        data: S?,
        valueType: Store.ValueType<S>
    ) {
        if (data == null) {
            editor.remove(key)
            return
        }

        when (valueType) {
            is Store.ValueType.Long -> editor.putLong(key, data as Long)
            is Store.ValueType.Float -> editor.putFloat(key, data as Float)
            is Store.ValueType.Int -> editor.putInt(key, data as Int)
            is Store.ValueType.Boolean -> editor.putBoolean(key, data as Boolean)
            is Store.ValueType.String -> editor.putString(key, data as String)
            is Store.ValueType.StringSet -> editor.putStringSet(key, data as Set<String>)
            is Store.ValueType.Object -> editor.putString(
                key,
                jsonParser.toJson(data, valueType.type)
            )
        }
    }

    override suspend fun keys() = withContext(Dispatchers.IO) {
        prefs.all.keys.toList()
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            prefs.edit().clear().commit()
        }
    }

    override fun beginTransaction(): Transaction<String> = TransactionImpl()

    override suspend fun execute(block: suspend Transaction<String>.() -> Unit) {
        beginTransaction().apply {
            block()
            execute()
        }
    }

    override fun clone(name: String) = PrefsStore("${this.name}_$name", context, jsonParser)

    private inner class TransactionImpl : Transaction<String> {

        private val editor = prefs.edit()

        override suspend fun clear() {
            editor.clear()
        }

        override suspend fun remove(key: String) {
            editor.remove(key)
        }

        override suspend fun <S : Any> put(key: String, data: S?, valueType: Store.ValueType<S>) {
            putInEditor(editor, key, data, valueType)
        }

        override suspend fun execute() {
            withContext(Dispatchers.IO) {
                editor.commit()
            }
        }
    }
}