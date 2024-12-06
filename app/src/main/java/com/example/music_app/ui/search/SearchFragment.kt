package com.example.music_app.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.music_app.R
import com.example.music_app.core.constants.Converter
import com.example.music_app.data.remote.model.AlbumR
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.databinding.FragmentSearchBinding
import com.example.music_app.ui.search.adapter.GridRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.SearchState
import com.example.music_app.viewmodel.SearchViewModel


class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private var data: List<Any> = emptyList()
    private lateinit var rvAdapter: GridRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        rvAdapter = GridRVAdapter(data, ::onItemClick)

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
        viewModel.searchState.observe(viewLifecycleOwner) { response ->
            if (response is SearchState.Success) {
                when (binding.searchOptions.checkedRadioButtonId) {
                    R.id.tracks -> {
                        val tracks =
                            Converter.convertTrackRToTracks(response.result as List<TrackR>)
                        println("Observed data: $tracks")
                        rvAdapter.updateData(tracks)
                    }

                    R.id.albums -> {
                        val albums =
                            Converter.convertAlbumRToAlbums(response.result as List<AlbumR>)
                        println("Observed data: $albums")
                        rvAdapter.updateData(albums)
                    }
                }
            } else if (response is SearchState.Error) {
                println("Error: ${response.message}")
            }
        }
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
                R.id.tracks -> viewModel.searchByTrack(query)
                R.id.albums -> viewModel.searchByAlbum(query)
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