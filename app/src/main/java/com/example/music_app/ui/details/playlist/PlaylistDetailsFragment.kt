package com.example.music_app.ui.details.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.databinding.FragmentPlaylistDetailsBinding
import com.example.music_app.ui.details.album.adapter.TrackRVAdapter
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.PlaylistViewModel


class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: PlaylistDetailsFragmentArgs by navArgs()
    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var rvAdapter: TrackRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)

        val playlist: Playlist = args.playlist
        rvAdapter = TrackRVAdapter(playlist.tracks, ::onTrackClick)

        bindData(playlist)
        setGridRv()

        return binding.root
    }

    private fun bindData(playlist: Playlist) {
        binding.toolbar.title = playlist.name
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
            PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToTrackDetailsFragment(
                track
            )
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}