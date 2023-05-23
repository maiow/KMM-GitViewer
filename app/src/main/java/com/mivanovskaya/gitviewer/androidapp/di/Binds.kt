package com.mivanovskaya.gitviewer.androidapp.di

import com.mivanovskaya.gitviewer.androidapp.data.AppRepositoryImpl
import com.mivanovskaya.gitviewer.androidapp.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Binds {

    @Binds
    fun bindsAppRepository(repository: AppRepositoryImpl): AppRepository
}