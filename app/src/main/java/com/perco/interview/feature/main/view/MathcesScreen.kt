package com.perco.interview.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(viewModel: MatchesScreenViewModel = getViewModel()) {
    val matches by viewModel.state.collectAsState()
    if (matches.isSuccess) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(matches.getOrThrow()) {
                MatchView(it)
            }
        }
    } else {
        Text(text = "Failed to load: ${matches.exceptionOrNull()}")
    }
}

@Composable
fun MatchView(match: Match) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreView(modifier = Modifier.weight(1f), match.homeTeam, match.homeScore)
        ScoreView(modifier = Modifier.weight(1f), match.awayTeam, match.awayScore)
    }
}

@Composable
fun ScoreView(modifier: Modifier = Modifier, team: String, score: Int) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = team, color = Color.White, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "$score", color = Color.White)
    }
}
