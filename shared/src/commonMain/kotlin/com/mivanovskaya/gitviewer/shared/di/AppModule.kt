package com.mivanovskaya.gitviewer.shared.di

import com.mivanovskaya.gitviewer.shared.KeyValueStorage
import com.mivanovskaya.gitviewer.shared.data.AppRepositoryImpl
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

fun appModule() = module {
    single<AppRepository> {
        AppRepositoryImpl(
            ioDispatcher = Dispatchers.IO,
            keyValueStorage = KeyValueStorage
        )
    }
}