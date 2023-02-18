package com.perco.interview.feature.main.repo.remoteModel

import com.perco.interview.core.repo.repos.DataMapper
import com.perco.interview.feature.main.model.Game
import com.perco.interview.feature.main.model.Games

class GamesResponseDataMapper : DataMapper<GamesResponse, Games> {

    override suspend fun map(data: GamesResponse) =
        Games(
            data.games
                .filter { game ->
                    game.wscGame?.primeStory?.pages?.lastOrNull().let {
                        it?.homeScore != null && it.awayScore != null
                    }
                }
                .map { game ->
                    val lastPage = game.wscGame?.primeStory?.pages?.last()!!
                    Game(
                        id = game.wscGameId,
                        homeTeam = game.teams.home.name,
                        awayTeam = game.teams.away.name,
                        homeScore = lastPage.homeScore!!,
                        awayScore = lastPage.awayScore!!
                    )
                }
        )
}