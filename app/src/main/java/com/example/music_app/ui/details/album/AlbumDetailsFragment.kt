package com.example.music_app.ui.details.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.core.constants.Converter
import com.example.music_app.core.constants.SharedFunctions
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.databinding.FragmentAlbumDetailsBinding
import com.example.music_app.ui.details.album.adapter.TrackRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.SearchState
import com.example.music_app.viewmodel.SearchViewModel

class AlbumDetailsFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailsFragmentArgs by navArgs()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var rvAdapter: TrackRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        val album: Album = args.album
        rvAdapter = TrackRVAdapter(album.tracks, ::onTrackClick)

        bindData(album)
        setGridRv()
        getAlbumTracks(album)

        return binding.root
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