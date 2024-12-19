package com.example.music_app.ui.details.track

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.R
import com.example.music_app.core.SharedFunctions
import com.example.music_app.databinding.FragmentTrackDetailsBinding
import com.example.music_app.ui.search.data.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        Log.e("TrackDetailsFragment", "Track: ${track.preview}")
        MusicController.setMediaItem(requireContext(), track.preview)
        MusicController.setVolume(1f)
        playMusic()
        return binding.root
    }


    private fun playMusic() {
        binding.playPauseButton.setOnClickListener {
            if (MusicController.isPlaying()) {
                MusicController.pause()
                binding.playPauseButton.setImageResource(R.drawable.play_music_icon)
            } else {
                MusicController.playPreview()
                binding.playPauseButton.setImageResource(R.drawable.stop_music_icon)
            }
        }
        setupSeekBar()
    }

    private fun setupSeekBar() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                val currentPosition = MusicController.getCurrentPosition()
                val duration = MusicController.getDuration()

                if (duration > 0) {
                    binding.seekBar.max = duration.toInt()
                    binding.seekBar.progress = currentPosition.toInt()
                    binding.currentDuration.text = formatTime(currentPosition)
                    binding.totalDuration.text = formatTime(duration)
                }

                delay(8)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicController.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }


    private fun bindData(track: Track) {
        SharedFunctions.loadImageFromUrl(binding.trackCoverImage, track.track_image)
        binding.trackTitle.text = track.track_title
        binding.trackArtist.text = track.artist_name
        binding.toolbar.title = track.track_title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MusicController.stop()
        _binding = null
    }
}