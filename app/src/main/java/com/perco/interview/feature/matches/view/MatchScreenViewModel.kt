package com.perco.interview.feature.matches.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perco.interview.core.extension.mapSuccess
import com.perco.interview.feature.matches.repo.GamesRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MatchScreenViewModel(repo: GamesRepo, matchId: String) : ViewModel() {

    val state = repo.getById(matchId)
        .mapSuccess { res ->
            res.videos
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = Result.success(emptyList())
        )
}