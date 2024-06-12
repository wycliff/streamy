package com.movies.streamy.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FloatingViewRepository {
    private val _appInForegroundState = MutableLiveData<Boolean>()
    val appInForegroundState: LiveData<Boolean> get() = _appInForegroundState

    fun updateForegroundStatus(isInForeground: Boolean) {
        _appInForegroundState.value = isInForeground
    }
}