package com.perco.interview.feature.matches.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MatchScreen(
    matchId: String,
    viewModel: MatchScreenViewModel = getViewModel(parameters = { parametersOf(matchId) })
) {
    val match by viewModel.state.collectAsState()
    if (match.isSuccess) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(match.getOrThrow()) {
                Text(text = it)
            }
        }
    } else {
        Text(text = "Failed to load: ${match.exceptionOrNull()}")
    }
}