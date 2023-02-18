package com.perco.interview.core.assets

import android.content.Context

interface AssetReader {

    fun read(file: String): Result<String>
}

class AssetReaderImpl(private val context: Context) : AssetReader {

    override fun read(file: String) = runCatching {
        context.assets.open(file).bufferedReader().use { it.readText() }
    }
}