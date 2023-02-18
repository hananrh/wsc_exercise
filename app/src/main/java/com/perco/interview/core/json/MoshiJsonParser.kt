package com.perco.interview.core.json

import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class MoshiJsonParser(private val moshi: Moshi) : JsonParser {

    override fun <T> fromJson(json: String, type: Type) = adapter<T>(type).fromJson(json)

    override fun <T> toJson(obj: T, type: Type): String = moshi.adapter<T>(type).toJson(obj)

    private fun <T> adapter(type: Type) = moshi.adapter<T>(type)
}