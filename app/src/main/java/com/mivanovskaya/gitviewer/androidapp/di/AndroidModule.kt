package com.mivanovskaya.gitviewer.androidapp.di

import com.mivanovskaya.gitviewer.androidapp.presentation.auth.AuthViewModel
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.RepositoriesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val androidModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::RepositoriesListViewModel)
}