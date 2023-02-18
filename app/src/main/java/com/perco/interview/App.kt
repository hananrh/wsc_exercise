package com.perco.interview

import android.app.Application
import com.perco.interview.core.assets.di.assetsModule
import com.perco.interview.core.exo.di.videoPlayerModule
import com.perco.interview.core.json.di.jsonModule
import com.perco.interview.core.repo.di.repoModule
import com.perco.interview.feature.matches.di.mainScreenModule
import com.perco.interview.logging.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            allowOverride(true)
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    jsonModule,
                    mainScreenModule,
                    assetsModule,
                    repoModule,
                    videoPlayerModule
                )
            )
        }

        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
    }
}