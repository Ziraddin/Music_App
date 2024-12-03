package com.example.music_app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.music_app.R
import com.example.music_app.databinding.FragmentSearchBinding
import com.example.music_app.ui.search.adapter.GridRVAdapter
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.SampleData
import com.example.music_app.ui.search.data.Track


class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val data1: List<Track> = SampleData.generateTracks(5)
        val data2: List<Album> = SampleData.generateAlbums(5)
        val data = data1 + data2
        val rvAdapter: GridRVAdapter = GridRVAdapter(data, ::onItemClick)
        setUpGridRv(rvAdapter)
        return binding.root
    }


    private fun setUpGridRv(rvAdapter: GridRVAdapter) {
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