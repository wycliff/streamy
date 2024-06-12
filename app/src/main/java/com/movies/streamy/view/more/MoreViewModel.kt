package com.movies.streamy.view.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.repository.abstraction.IMoreRepository
import com.movies.streamy.model.repository.implementation.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val moreRepository: IMoreRepository,
    private val homeRepository: HomeRepositoryImpl,
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "More"
    }
    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            moreRepository.logout()
        }
    }

}