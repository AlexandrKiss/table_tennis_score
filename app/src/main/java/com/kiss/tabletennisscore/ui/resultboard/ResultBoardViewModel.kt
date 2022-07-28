package com.kiss.tabletennisscore.ui.resultboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiss.tabletennisscore.common.Result
import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultBoardViewModel
@Inject constructor(private val repository: ResultRepository): ViewModel() {
    private val _gameListFlow = MutableStateFlow<Result<List<Game>>>(Result.Loading())
    val gameListFlow: StateFlow<Result<List<Game>>>
        get() = _gameListFlow

    fun getGameList() {
        viewModelScope.launch(Dispatchers.IO) {
            val gameList = repository.getResultList().reversed()
            if (gameList.isNotEmpty()) _gameListFlow.emit(Result.Success(gameList))
            else Result.Empty<List<Game>>()
        }
    }
}