package com.perco.interview.feature.matches

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import com.perco.interview.feature.matches.view.MatchScreen
import com.perco.interview.feature.matches.view.MatchesScreen

private sealed class Page {
    object Matches : Page()
    data class Match(val id: String) : Page()
}

@Composable
fun MainScreen() {
    var page: Page by remember {
        mutableStateOf(Page.Matches)
    }

    BackHandler(page is Page.Match) {
        page = Page.Matches
    }

    when (val pageValue = page) {
        is Page.Matches ->
            MatchesScreen(onMatchClicked = {
                page = Page.Match(it.id)
            })
        is Page.Match -> {
            MatchScreen(pageValue.id)
        }
    }
}