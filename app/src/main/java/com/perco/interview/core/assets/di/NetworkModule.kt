package com.perco.interview.core.assets.di

import com.perco.interview.core.assets.AssetReader
import com.perco.interview.core.assets.AssetReaderImpl
import org.koin.dsl.module

val assetsModule = module {

    single<AssetReader> {
        AssetReaderImpl(get())
    }
}