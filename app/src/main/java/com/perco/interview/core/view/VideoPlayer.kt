package com.perco.interview.core.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import org.koin.androidx.compose.get

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    url: String,
    play: Boolean,
    onPlaybackDone: () -> Unit = {},
    player: ExoPlayer = get(),
    mediaSourceFactory: MediaSource.Factory = get()
) {
    val exoPlayer = remember {
        player.apply {
            playWhenReady = play
            setMediaSource(
                mediaSourceFactory.createMediaSource(MediaItem.fromUri(url))
            )
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_ENDED) {
                        onPlaybackDone()
                    }
                }
            })
            prepare()
        }
    }

    LaunchedEffect(play) {
        if (play) {
            exoPlayer.play()
        } else {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            }
        }
    }

    LifecycleEffect(exoPlayer, play)

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                StyledPlayerView(it).apply {
                    this.player = exoPlayer
                    this.useController = false
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
private fun LifecycleEffect(
    exoPlayer: ExoPlayer,
    play: Boolean,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(play) {
        val observer = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                }
            }

            override fun onResume(owner: LifecycleOwner) {
                if (play) {
                    exoPlayer.play()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}