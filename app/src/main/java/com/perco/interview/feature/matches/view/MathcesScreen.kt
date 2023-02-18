package com.perco.interview.feature.matches.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun MatchesScreen(
    viewModel: MatchesScreenViewModel = getViewModel(),
    onMatchClicked: (Match) -> Unit
) {
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
                MatchView(modifier = Modifier.clickable { onMatchClicked(it) }, it)
            }
        }
    } else {
        Text(text = "Failed to load: ${matches.exceptionOrNull()}")
    }
}

@Composable
fun MatchView(modifier: Modifier = Modifier, match: Match) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Blue)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreView(modifier = Modifier.weight(1f), match.homeScore)
        ScoreView(modifier = Modifier.weight(1f), match.awayScore)
    }
}

@Composable
fun ScoreView(modifier: Modifier = Modifier, score: TeamScore) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = score.team, color = Color.White, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "${score.score}", color = Color.White)
    }
}
