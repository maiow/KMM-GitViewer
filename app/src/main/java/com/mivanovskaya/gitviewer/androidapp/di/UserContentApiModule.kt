//package com.mivanovskaya.gitviewer.androidapp.di
//
//import com.mivanovskaya.gitviewer.shared.data.api.UserContentApi
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import retrofit2.Retrofit
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class UserContentApiModule {
//
//    @Provides
//    @Singleton
//    fun provideUserContentApi(): UserContentApi = Retrofit.Builder()
//        .baseUrl("https://raw.githubusercontent.com")
//        .addConverterFactory(ScalarsConverterFactory.create())
//        .build().create(UserContentApi::class.java)
//}