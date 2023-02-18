package com.perco.interview.feature.matches.repo

import com.perco.interview.core.extension.mapSuccess
import com.perco.interview.core.repo.Repo
import com.perco.interview.feature.matches.model.Game
import com.perco.interview.feature.matches.model.Games
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GamesRepo {
    fun getAll(): Flow<Result<Games>>
    fun getById(gameId: String): Flow<Result<Game>>
}

class GamesRepoImpl(private val repo: Repo<String, Games>) : GamesRepo {

    override fun getAll() = repo.getData("")

    override fun getById(gameId: String) = getAll()
        .mapSuccess { it.gameList }
        .map { res ->
            if (res.isSuccess) res.getOrThrow().find { it.id == gameId }?.let { Result.success(it) }
                ?: Result.failure(Exception("Game not found"))
            else Result.failure(res.exceptionOrNull()!!)
        }
}