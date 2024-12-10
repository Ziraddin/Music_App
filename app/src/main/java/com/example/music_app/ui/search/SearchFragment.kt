package com.example.music_app.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.music_app.R
import com.example.music_app.core.constants.Converter
import com.example.music_app.data.remote.model.AlbumR
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.databinding.BottomSheetSelectPlaylistBinding
import com.example.music_app.databinding.FragmentSearchBinding
import com.example.music_app.ui.main.MainActivity
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.playlist.adapter.PlaylistRVAdapter
import com.example.music_app.ui.search.adapter.GridRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.PlaylistState
import com.example.music_app.viewmodel.PlaylistViewModel
import com.example.music_app.viewmodel.SearchState
import com.example.music_app.viewmodel.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModelSearch: SearchViewModel by viewModels()
    private lateinit var playlistViewModel: PlaylistViewModel
    private var data: List<Any> = emptyList()
    private lateinit var rvAdapter: GridRVAdapter
    private lateinit var playlistRVAdapter: PlaylistRVAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var pressedTrack: Track

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        playlistViewModel = (activity as MainActivity).playlistViewModel
        rvAdapter = GridRVAdapter(data, ::onItemClick, ::onLongPress)
        playlistRVAdapter =
            PlaylistRVAdapter(emptyList(), onPlaylistSelected = ::onPlaylistSelected)
        bottomSheetDialog = createBottomSheet()

        setGridRv(rvAdapter)
        search()

        return binding.root
    }

    private fun search() {
        setSearchButton()
        radioButtonChange()
        observeResults()
    }

    private fun observeResults() {
        viewModelSearch.searchState.observe(viewLifecycleOwner) { response ->
            if (response is SearchState.Success) {
                val dataList = when (binding.searchOptions.checkedRadioButtonId) {
                    R.id.tracks -> Converter.convertTrackRToTracks(response.result as List<TrackR>)
                    R.id.albums -> Converter.convertAlbumRToAlbums(response.result as List<AlbumR>)
                    else -> emptyList()
                }

                if (dataList.isEmpty()) {
                    showLottieAnimation()
                } else {
                    rvAdapter.updateData(dataList)
                    showRecyclerView()
                }
            } else if (response is SearchState.Error) {
                println("Error: ${response.message}")
                showLottieAnimation()
            }
        }
    }

    private fun showLottieAnimation() {
        binding.lottieView.visibility = View.VISIBLE
        binding.recyclerViewResults.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.lottieView.visibility = View.GONE
        binding.recyclerViewResults.visibility = View.VISIBLE
    }


    private fun radioButtonChange() {
        val handler = Handler(Looper.getMainLooper())
        var runnable: Runnable? = null

        binding.searchOptions.setOnCheckedChangeListener { _, checkedId ->
            runnable?.let { handler.removeCallbacks(it) }
            runnable = Runnable {
                requestSearch(checkedId)
            }
            handler.postDelayed(runnable!!, 50)
        }
    }

    private fun setSearchButton() {
        binding.btnSearch.setOnClickListener {
            val radioId: Int = binding.searchOptions.checkedRadioButtonId
            requestSearch(radioId)
            clearSearchBar()
        }
    }

    private fun requestSearch(radioId: Int) {
        val query = binding.searchBar.query.toString()
        if (query.isNotEmpty()) {
            when (radioId) {
                R.id.tracks -> viewModelSearch.searchByTrack(query)
                R.id.albums -> viewModelSearch.searchByAlbum(query)
            }
        }
    }

    private fun clearSearchBar() {
        binding.searchBar.clearFocus()
    }

    private fun setSearchView() {}

    private fun setGridRv(rvAdapter: GridRVAdapter) {
        binding.recyclerViewResults.adapter = rvAdapter
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

    private fun onItemClick(item: Any) {
        val navController = findNavController()
        when (item) {
            is Track -> {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToTrackDetailsFragment(item)
                navController.navigate(action)
            }

            is Album -> {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToAlbumDetailsFragment(item)
                navController.navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}