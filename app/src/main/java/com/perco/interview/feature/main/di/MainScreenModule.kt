package com.perco.interview.feature.main.di

import com.perco.interview.core.repo.Repo
import com.perco.interview.core.repo.local.cache.CachePolicy
import com.perco.interview.core.repo.local.cache.TTLCacheLocalDataSource
import com.perco.interview.core.repo.local.store.StoreLocalDataSource
import com.perco.interview.core.repo.remote.AssetDataSource
import com.perco.interview.core.repo.repos.RemoteCachedRepo
import com.perco.interview.core.store.asFlowStore
import com.perco.interview.core.store.prefs.PrefsStore
import com.perco.interview.feature.main.model.Games
import com.perco.interview.feature.main.repo.GamesResponse
import com.perco.interview.feature.main.repo.GamesResponseDataMapper
import com.perco.interview.feature.main.view.MatchesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.hours

val mainScreenModule = module {

    viewModel {
        MatchesScreenViewModel(get(named<Games>()))
    }

    single<Repo<String, Games>>(named<Games>()) {
        val name = "games"
        RemoteCachedRepo(
            name = name,
            localSource = TTLCacheLocalDataSource(
                StoreLocalDataSource(
                    flowStore = PrefsStore(name, get(), get()).asFlowStore(),
                    storeKeyResolver = { it },
                    dataType = Games::class.java
                ),
                cachePolicy = CachePolicy(
                    ttlMs = 12.hours.inWholeMilliseconds
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
    }
}