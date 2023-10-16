package com.mivanovskaya.gitviewer.shared.di

import com.mivanovskaya.gitviewer.shared.data.KeyValueStorage
import com.mivanovskaya.gitviewer.shared.data.AppRepositoryImpl
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

fun appModule() = module {
    single { KeyValueStorage() }
    single<Antilog> { DebugAntilog() }
    factory { Dispatchers.IO }
    single<AppRepository> {
        AppRepositoryImpl(
            ioDispatcher = get(),
            keyValueStorage = get(),
            antilog = get()
        )
    }
}