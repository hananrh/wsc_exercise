package com.perco.interview.feature.matches.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.perco.interview.core.view.VideoPlayer
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MatchScreen(
    matchId: String,
    viewModel: MatchScreenViewModel = getViewModel(parameters = { parametersOf(matchId) })
) {
    val match by viewModel.state.collectAsState()
    if (match.isSuccess) {
        val videos = match.getOrThrow()
        val pagerState = rememberPagerState()
        VerticalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(10.dp),
            count = videos.size,
            state = pagerState
        ) { page ->
            VideoPlayer(
//                modifier = Modifier.fillMaxSize(),
                url = videos[page],
                play = pagerState.currentPage == page
            )
        }
    } else {
        Text(text = "Failed to load: ${match.exceptionOrNull()}")
    }
}