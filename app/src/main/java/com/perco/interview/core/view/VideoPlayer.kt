package com.perco.interview.core.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import timber.log.Timber

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, url: String, play: Boolean) {
    val context = LocalContext.current

    Timber.d("SHIT Video = $url, play = $play")

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = play
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }

    LaunchedEffect(play) {
        if (play) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                StyledPlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}