package com.example.music_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.search.data.Track
import kotlinx.coroutines.launch

sealed class PlaylistState {
    data object Idle : PlaylistState()
    data object Loading : PlaylistState()
    data class Success(val result: List<Playlist>) : PlaylistState()
    data class Error(val message: String) : PlaylistState()
}

class PlaylistViewModel : ViewModel() {
    private val _playlistState = MutableLiveData<PlaylistState>(PlaylistState.Idle)
    val playlistState: MutableLiveData<PlaylistState> = this._playlistState

    private val playlists =
        mutableListOf<Playlist>()

    fun getPlaylists() {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                _playlistState.value = PlaylistState.Success(playlists)
            } catch (e: Exception) {
                _playlistState.value = PlaylistState.Error("Failed to load playlists: ${e.message}")
            }
        }
    }

    fun addPlaylist(playlist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                playlists.add(playlist)
                _playlistState.value = PlaylistState.Success(playlists)
            } catch (e: Exception) {
                _playlistState.value = PlaylistState.Error("Failed to add playlist: ${e.message}")
            }
        }
    }

    fun removePlaylist(playlist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                playlists.remove(playlist)
                _playlistState.value = PlaylistState.Success(playlists)
            } catch (e: Exception) {
                _playlistState.value =
                    PlaylistState.Error("Failed to remove playlist: ${e.message}")
            }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                val index = playlists.indexOfFirst { it.id == playlist.id }
                if (index != -1) {
                    playlists[index] = playlist
                    _playlistState.value = PlaylistState.Success(playlists)
                } else {
                    _playlistState.value = PlaylistState.Error("Playlist not found")
                }
            } catch (e: Exception) {
                _playlistState.value =
                    PlaylistState.Error("Failed to update playlist: ${e.message}")
            }
        }
    }

    fun addTrackToPlaylist(track: Track, selectedPlaylist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                val index = playlists.indexOfFirst { it.id == selectedPlaylist.id }
                if (index != -1) {
                    playlists[index].tracks.add(track)
                    _playlistState.value = PlaylistState.Success(playlists)
                } else {
                    _playlistState.value = PlaylistState.Error("Playlist not found")
                }
            } catch (e: Exception) {
                _playlistState.value =
                    PlaylistState.Error("Failed to add track to playlist: ${e.message}")
            }
        }
    }
}
