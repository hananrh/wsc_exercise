package com.perco.interview.core.exo.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import org.koin.dsl.module
import java.io.File

val videoPlayerModule = module {

    factory {
        ExoPlayer.Builder(get()).build()
    }

    single<MediaSource.Factory> {
        val context = get<Context>()

        val downloadCache = SimpleCache(
            File(context.cacheDir, "media"),
            LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong()),
            StandaloneDatabaseProvider(context)
        )

        ProgressiveMediaSource.Factory(
            CacheDataSource.Factory()
                .setCache(downloadCache)
                .setCacheWriteDataSinkFactory(
                    CacheDataSink.Factory()
                        .setCache(downloadCache)
                )
                .setCacheReadDataSourceFactory(FileDataSource.Factory())
                .setUpstreamDataSourceFactory(
                    DefaultDataSource.Factory(
                        context,
                        DefaultHttpDataSource.Factory()
                    )
                )
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        )
    }
}