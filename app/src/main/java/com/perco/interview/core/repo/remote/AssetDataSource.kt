package com.perco.interview.core.repo.remote

import com.perco.interview.core.assets.AssetReader
import com.perco.interview.core.json.JsonParser
import com.shire.robin.repo.base.remote.RemoteDataSource
import okio.IOException

class AssetDataSource<T : Any, S>(
    private val assetReader: AssetReader,
    private val jsonParser: JsonParser,
    private val fileName: String,
    private val jsonClass: Class<S>
) : RemoteDataSource<T, S> {

    override suspend fun getData(request: T): Result<S> {
        val res = assetReader.read(fileName)
        if (res.isFailure) {
            return Result.failure(res.exceptionOrNull() ?: IOException("Failed to read asset"))
        }

        return jsonParser.fromJson<S>(res.getOrThrow(), jsonClass)?.let {
            Result.success(it)
        } ?: Result.failure(IOException("Failed to read asset"))
    }

    override fun toString(): String = javaClass.simpleName
}