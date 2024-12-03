package com.example.music_app.ui.details.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.music_app.databinding.FragmentTrackDetailsBinding
import com.example.music_app.ui.search.data.Track


class TrackDetailsFragment : Fragment() {

    private var _binding: FragmentTrackDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: TrackDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackDetailsBinding.inflate(inflater, container, false)

        val track: Track = args.track
        bindData(track)

        return binding.root
    }


    private fun bindData(track: Track) {
        binding.trackCoverImage.setImageResource(track.album_image)
        binding.trackTitle.text = track.track_title
        binding.trackArtist.text = track.artist_name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}