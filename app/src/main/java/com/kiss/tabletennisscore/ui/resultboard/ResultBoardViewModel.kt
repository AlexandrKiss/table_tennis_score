package com.kiss.tabletennisscore.ui.resultboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultBoardViewModel
@Inject constructor(private val repository: ResultRepository): ViewModel() {
    private val _gameListLiveData = MutableLiveData<List<Game>>()
    val gameListLiveData: LiveData<List<Game>>
        get() = _gameListLiveData

    fun getGameList() {
        viewModelScope.launch(Dispatchers.IO) {
            _gameListLiveData.postValue(repository.getResultList().reversed())
        }
    }
}