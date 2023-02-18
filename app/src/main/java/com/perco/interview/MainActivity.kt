package com.perco.interview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.perco.interview.feature.matches.MainScreen
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