package com.perco.interview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.perco.interview.feature.main.view.MainScreen
import com.perco.interview.theme.InterviewExerciseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterviewExerciseTheme {
                MainScreen()
            }
        }
    }
}