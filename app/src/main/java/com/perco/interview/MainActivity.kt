package com.perco.interview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.perco.interview.feature.matches.view.MatchScreen
import com.perco.interview.feature.matches.view.MatchesScreen
import com.perco.interview.theme.WSCExerciseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WSCExerciseTheme {
                MainScreen()
            }
        }
    }
}

sealed class Page {
    object Matches : Page()
    data class Match(val id: String) : Page()
}

@Composable
private fun MainScreen() {
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