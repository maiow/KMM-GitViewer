package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectInStartedState(
    flow: Flow<T>,
    action: (it: T) -> Unit
): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { action(it) }
        }
    }
}
