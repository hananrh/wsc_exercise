package com.perco.interview.feature.matches.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perco.interview.core.extension.mapSuccess
import com.perco.interview.feature.matches.repo.GamesRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MatchesScreenViewModel(repo: GamesRepo) : ViewModel() {

    val state = repo.getAll()
        .mapSuccess { res ->
            res.gameList.map {
                Match(
                    id = it.id,
                    homeScore = TeamScore(it.homeTeam, it.homeScore),
                    awayScore = TeamScore(it.awayTeam, it.awayScore)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = Result.success(emptyList())
        )
}

data class Match(
    val id: String,
    val homeScore: TeamScore,
    val awayScore: TeamScore
)

data class TeamScore(val team: String, val score: Int)