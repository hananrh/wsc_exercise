package com.perco.interview.feature.main.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perco.interview.core.extension.mapSuccess
import com.perco.interview.core.repo.Repo
import com.perco.interview.feature.main.model.Games
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MatchesScreenViewModel(repo: Repo<String, Games>) : ViewModel() {

    val state = repo.getData("")
        .mapSuccess { res ->
            res.gameList.map {
                Match(it.id, it.homeTeam, it.awayTeam, it.homeScore, it.awayScore)
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
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int
)