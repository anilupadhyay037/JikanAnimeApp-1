package com.ktorlib.jikananimeapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktorlib.jikananimeapp.data.repository.AnimeRepository
import com.ktorlib.jikananimeapp.ui.common.UiState
import com.ktorlib.jikananimeapp.util.NetworkUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeListViewModel(private val repo: AnimeRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val uiState: StateFlow<UiState<Unit>> = _uiState

    val anime = repo.anime

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repo.refresh()

            _uiState.value =
                if (result.isSuccess) {
                    UiState.Success(Unit)
                } else {
                    UiState.Error("Offline mode: showing cached data")
                }
        }
    }
}