package com.perco.interview.feature.matches.di

import com.perco.interview.core.repo.local.cache.CachePolicy
import com.perco.interview.core.repo.local.cache.TTLCacheLocalDataSource
import com.perco.interview.core.repo.local.store.StoreLocalDataSource
import com.perco.interview.core.repo.remote.AssetDataSource
import com.perco.interview.core.repo.repos.RemoteCachedRepo
import com.perco.interview.core.store.asFlowStore
import com.perco.interview.core.store.prefs.PrefsStore
import com.perco.interview.feature.matches.model.Games
import com.perco.interview.feature.matches.repo.GamesRepo
import com.perco.interview.feature.matches.repo.GamesRepoImpl
import com.perco.interview.feature.matches.repo.remoteModel.GamesResponse
import com.perco.interview.feature.matches.repo.remoteModel.GamesResponseDataMapper
import com.perco.interview.feature.matches.view.MatchScreenViewModel
import com.perco.interview.feature.matches.view.MatchesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes

val mainScreenModule = module {

    viewModel {
        MatchesScreenViewModel(get())
    }

    viewModel { params ->
        MatchScreenViewModel(get(), params.get())
    }

    single<GamesRepo> {
        val name = "games"
        GamesRepoImpl(
            RemoteCachedRepo(
                name = name,
                localSource = TTLCacheLocalDataSource(
                    StoreLocalDataSource(
                        flowStore = PrefsStore(name, get(), get()).asFlowStore(),
                        storeKeyResolver = { it },
                        dataType = Games::class.java
                    ),
                    cachePolicy = CachePolicy(
                        ttlMs = 10.minutes.inWholeMilliseconds
                    ),
                    requestKeyResolver = { it },
                    timestampStore = PrefsStore("${name}_timestamps", get(), get())
                ),
                remoteSource = AssetDataSource(
                    get(),
                    get(),
                    "response.json",
                    GamesResponse::class.java
                ),
                remoteDataMapper = GamesResponseDataMapper(),
                scope = get(named("repo"))
            )
        )
    }
}