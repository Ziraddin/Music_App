package com.example.music_app.ui.details.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.databinding.FragmentAlbumDetailsBinding
import com.example.music_app.ui.details.album.adapter.TrackRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track

class AlbumDetailsFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)

        val album: Album = args.album
        bindData(album)

        val data: List<Track> = album.tracks
        val rvAdapter: TrackRVAdapter = TrackRVAdapter(data, ::onTrackClick)
        setUpGridRv(rvAdapter)

        return binding.root
    }

    private fun bindData(album: Album) {
        binding.albumCoverImage.setImageResource(album.album_image)
        binding.albumTitle.text = album.album_title
        binding.albumArtist.text = album.artist_name
    }

    private fun setUpGridRv(rvAdapter: TrackRVAdapter) {
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