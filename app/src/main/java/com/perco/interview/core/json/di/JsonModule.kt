package com.perco.interview.core.json.di

import com.perco.interview.core.json.JsonParser
import com.perco.interview.core.json.MoshiJsonParser
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val jsonModule = module {
    single {
        Moshi.Builder()
            .build()
    }

    single<JsonParser> { MoshiJsonParser(get()) }
}