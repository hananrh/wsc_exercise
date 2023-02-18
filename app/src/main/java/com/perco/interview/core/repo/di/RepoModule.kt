package com.perco.interview.core.repo.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repoModule = module {
    single(named("repo")) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}