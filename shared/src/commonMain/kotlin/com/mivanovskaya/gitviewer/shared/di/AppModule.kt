package com.mivanovskaya.gitviewer.shared.di

import com.mivanovskaya.gitviewer.shared.data.AppRepositoryImpl
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import org.koin.dsl.module

fun appModule() = module {
    single<AppRepository> { AppRepositoryImpl() }
}