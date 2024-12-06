package com.example.music_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.data.repository.DeezerRepository
import kotlinx.coroutines.launch

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val result: List<Any>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class SearchViewModel(private val repository: DeezerRepository = DeezerRepository()) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>(SearchState.Idle)
    val searchState: MutableLiveData<SearchState> = this._searchState


    fun searchByAlbum(albumQuery: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val response = repository.getAlbums(albumQuery)
                if (response.isSuccessful) {
                    val albums = response.body()
                    albums?.let {
                        _searchState.value = SearchState.Success(it.data)
                        println(it.data)
                    } ?: run {
                        _searchState.value = SearchState.Error("No data found.")
                    }
                } else {
                    _searchState.value = SearchState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    fun getAlbumTracks(trackId: Int) {
        viewModelScope.launch {
            try {
                println("getAlbumTracks: request sent")
                val response = repository.getTracksById(trackId)
                if (response.isSuccessful) {
                    val tracks = response.body()?.data
                    tracks?.let {
                        _searchState.value = SearchState.Success(it)
                        println(it)
                    } ?: run {
                        _searchState.value = SearchState.Error("No data found.")
                    }
                } else {
                    _searchState.value = SearchState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }


    fun searchByTrack(trackQuery: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val response = repository.getTracks(trackQuery)
                if (response.isSuccessful) {
                    val tracks = response.body()
                    tracks?.let {
                        _searchState.value = SearchState.Success(it.data)
                        println(it.data)
                    } ?: run {
                        _searchState.value = SearchState.Error("No data found.")
                    }
                } else {
                    _searchState.value = SearchState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

}
