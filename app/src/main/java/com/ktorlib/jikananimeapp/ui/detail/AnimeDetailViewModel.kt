package com.ktorlib.jikananimeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ktorlib.jikananimeapp.data.repository.AnimeRepository

class AnimeDetailViewModel(
    private val repo: AnimeRepository
) : ViewModel() {

    fun load(id: Int) = liveData {
        emit(repo.getDetails(id))
    }
}