package com.movies.streamy.view.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.repository.abstraction.ISessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ISessionRepository,
    application: Application,
) : AndroidViewModel(application) {

    val currentUser: LiveData<UserEntity?>
        get() = repository.getCurrentUser(0).asLiveData()
}