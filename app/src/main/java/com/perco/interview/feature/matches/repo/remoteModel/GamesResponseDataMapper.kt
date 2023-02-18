package com.perco.interview.feature.matches.repo.remoteModel

import com.perco.interview.core.utils.ValueMapper
import com.perco.interview.feature.matches.model.Game
import com.perco.interview.feature.matches.model.Games

class GamesResponseDataMapper : ValueMapper<GamesResponse, Games> {

    override suspend fun map(value: GamesResponse) =
        Games(
            value.games
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
                        awayScore = lastPage.awayScore!!,
                        videos = game.wscGame.primeStory.pages.mapNotNull { it.videoUrl }
                    )
                }
        )
}