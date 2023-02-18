package com.perco.interview.feature.matches.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.perco.interview.core.view.VideoPlayer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MatchScreen(
    matchId: String,
    viewModel: MatchScreenViewModel = getViewModel(
        key = matchId,
        parameters = { parametersOf(matchId) })
) {
    val matchVideos by viewModel.state.collectAsState()
    if (matchVideos.isSuccess) {
        val videos = matchVideos.getOrThrow()
        if (videos.isNotEmpty()) {
            VideosPager(videos)
        }
    } else {
        Text(text = "Failed to load: ${matchVideos.exceptionOrNull()}")
    }
}

@Composable
@OptIn(ExperimentalPagerApi::class)
private fun VideosPager(
    videos: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    VerticalPager(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp),
        count = Int.MAX_VALUE, // Endless feed
        state = pagerState
    ) { page ->
        VideoPlayer(
            url = videos[page % videos.size],
            play = pagerState.currentPage == page,
            onPlaybackDone = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page + 1)
                }
            }
        )
    }
}