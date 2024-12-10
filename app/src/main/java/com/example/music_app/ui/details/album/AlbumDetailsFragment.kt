package com.example.music_app.ui.details.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.core.constants.Converter
import com.example.music_app.core.constants.SharedFunctions
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.databinding.BottomSheetSelectPlaylistBinding
import com.example.music_app.databinding.FragmentAlbumDetailsBinding
import com.example.music_app.ui.details.album.adapter.TrackRVAdapter
import com.example.music_app.ui.main.MainActivity
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.playlist.adapter.PlaylistRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.PlaylistState
import com.example.music_app.viewmodel.PlaylistViewModel
import com.example.music_app.viewmodel.SearchState
import com.example.music_app.viewmodel.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class AlbumDetailsFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailsFragmentArgs by navArgs()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var rvAdapter: TrackRVAdapter
    private lateinit var playlistRVAdapter: PlaylistRVAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var pressedTrack: Track

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        playlistViewModel = (activity as MainActivity).playlistViewModel
        val album: Album = args.album
        rvAdapter = TrackRVAdapter(album.tracks, ::onTrackClick, ::onLongPress)
        playlistRVAdapter =
            PlaylistRVAdapter(emptyList(), onPlaylistSelected = ::onPlaylistSelected)
        bottomSheetDialog = createBottomSheet()

        bindData(album)
        setGridRv()
        getAlbumTracks(album)

        return binding.root
    }

    private fun createBottomSheet(): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val binding =
            BottomSheetSelectPlaylistBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(binding.root)
        binding.recyclerViewPlaylists.adapter = playlistRVAdapter

        return bottomSheetDialog
    }


    private fun onLongPress(track: Track) {
        playlistViewModel.getPlaylists()
        playlistViewModel.playlistState.observe(viewLifecycleOwner) { response ->
            if (response is PlaylistState.Success) {
                val playlists = response.result
                println(playlists)
                playlistRVAdapter.updateData(playlists)
                pressedTrack = track
                bottomSheetDialog.show()

            } else if (response is PlaylistState.Error) {
                Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onPlaylistSelected(playlist: Playlist) {
        playlistViewModel.addTrackToPlaylist(pressedTrack, playlist)
        Toast.makeText(context, "Added to ${playlist.name}", Toast.LENGTH_SHORT).show()
        bottomSheetDialog.dismiss()
    }

    private fun getAlbumTracks(album: Album) {
        viewModel.getAlbumTracks(album.id)

        viewModel.searchState.observe(viewLifecycleOwner) { response ->
            if (response is SearchState.Success) {
                for (trackR in response.result as List<TrackR>) {
                    trackR.artist.picture_big = album.artist_image
                }
                album.tracks = Converter.convertTrackRToTracks(response.result as List<TrackR>)
                rvAdapter.updateData(album.tracks)
            } else if (response is SearchState.Error) {
                println("Error: ${response.message}")
            }
        }

    }

    private fun bindData(album: Album) {
        SharedFunctions.loadImageFromUrl(binding.albumCoverImage, album.album_image)
        binding.albumTitle.text = album.album_title
        binding.albumArtist.text = album.artist_name
        binding.toolbar.title = album.album_title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setGridRv() {
        binding.recyclerViewTracks.adapter = rvAdapter
    }

    private fun onTrackClick(track: Track) {
        val navController = findNavController()
        val action =
            AlbumDetailsFragmentDirections.actionAlbumDetailsFragmentToTrackDetailsFragment(track)
        navController.navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}