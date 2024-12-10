package com.example.music_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.data.local.entity.toPlaylist
import com.example.music_app.data.repository.MusicRepository
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.playlist.toPlaylistEntity
import com.example.music_app.ui.search.data.Track
import kotlinx.coroutines.launch

sealed class PlaylistState {
    data object Idle : PlaylistState()
    data object Loading : PlaylistState()
    data class Success(val result: List<Playlist>) : PlaylistState()
    data class Error(val message: String) : PlaylistState()
}

class PlaylistViewModel(private val repository: MusicRepository) : ViewModel() {
    private val _playlistState = MutableLiveData<PlaylistState>(PlaylistState.Idle)
    val playlistState: MutableLiveData<PlaylistState> = _playlistState

    fun getPlaylists() {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                val playlists = repository.getAllPlaylists().map { it.toPlaylist() }
                _playlistState.value = PlaylistState.Success(playlists)
                Log.d("PlaylistViewModel", "Playlists fetched: $playlists")
            } catch (e: Exception) {
                _playlistState.value = PlaylistState.Error("Failed to load playlists: ${e.message}")
            }
        }
    }


    fun addPlaylist(playlist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                val playlistEntity = playlist.toPlaylistEntity()
                repository.insertPlaylist(playlistEntity)
                getPlaylists()
            } catch (e: Exception) {
                _playlistState.value = PlaylistState.Error("Failed to add playlist: ${e.message}")
            }
        }
    }

    fun removePlaylist(playlist: Playlist) {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                repository.deletePlaylist(playlist.toPlaylistEntity())
                getPlaylists()
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
                repository.updatePlaylist(playlist.toPlaylistEntity())
                Log.d("PlaylistViewModel", "Playlist updated: $playlist")
                getPlaylists()
            } catch (e: Exception) {
                _playlistState.value =
                    PlaylistState.Error("Failed to update playlist: ${e.message}")
            }
        }
    }


    fun addTrackToPlaylist(track: Track, selectedPlaylist: Playlist) {
        Log.d("PlaylistViewModel", "addTrackToPlaylist started")

        viewModelScope.launch {
            try {
                val playlists = repository.getAllPlaylists().map { it.toPlaylist() }
                Log.d("PlaylistViewModel", "Playlists fetched: $playlists")

                val index = playlists.indexOfFirst { it.id == selectedPlaylist.id }
                if (index != -1) {
                    val playlist = playlists[index]

                    if (track in playlist.tracks) {
                        _playlistState.value = PlaylistState.Error("Track already in playlist")
                        return@launch
                    }

                    playlist.tracks.add(track)
                    playlist.trackCount = playlist.tracks.size

                    repository.updatePlaylist(playlist.toPlaylistEntity())
                    Log.d("PlaylistViewModel", "Track added to playlist: $track")

                } else {
                    _playlistState.value = PlaylistState.Error("Playlist not found")
                }
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error adding track to playlist", e)
                _playlistState.value =
                    PlaylistState.Error("Failed to add track to playlist: ${e.message}")
            }
        }
    }

}


