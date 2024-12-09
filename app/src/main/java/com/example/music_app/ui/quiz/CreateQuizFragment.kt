package com.example.music_app.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.music_app.R
import com.example.music_app.databinding.FragmentCreateQuizBinding
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.playlist.adapter.PlaylistRVAdapter
import com.example.music_app.ui.search.data.Track
import com.example.music_app.viewmodel.PlaylistState
import com.example.music_app.viewmodel.PlaylistViewModel
import com.example.music_app.viewmodel.QuizViewModel
import com.google.android.material.chip.Chip


class CreateQuizFragment : Fragment() {
    private var _binding: FragmentCreateQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by activityViewModels()
    private val playlistViewModel: PlaylistViewModel by activityViewModels()
    private val selectedPlaylists = mutableSetOf<Playlist>()
    private lateinit var rvAdapter: PlaylistRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateQuizBinding.inflate(inflater, container, false)
        rvAdapter = PlaylistRVAdapter(emptyList(), onPlaylistSelected = ::onPlaylistSelected)
        playlistViewModel.getPlaylists()

        observePlaylistState()
        setupPlaylistsRecyclerView()
        setupCreateQuizButton()
        navBack()
        return binding.root
    }


    private fun navBack() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observePlaylistState() {
        playlistViewModel.playlistState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Success -> {
                    rvAdapter.updateData(state.result)
                }

                is PlaylistState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun setupPlaylistsRecyclerView() {
        binding.recyclerViewPlaylists.adapter = rvAdapter
    }

    private fun onPlaylistSelected(playlist: Playlist) {
        if (selectedPlaylists.contains(playlist)) {
            selectedPlaylists.remove(playlist)
        } else {
            selectedPlaylists.add(playlist)
        }
        updateDynamicChip()
    }

    private fun updateDynamicChip() {
        val totalTracks = selectedPlaylists.sumOf { it.trackCount }
        val chips = listOf(3, 5, 7)

        for (i in 0 until binding.chipGroupQuestions.childCount) {
            val chip = binding.chipGroupQuestions.getChildAt(i)
            val chipValue = chips.getOrNull(i) ?: continue
            chip.isEnabled = totalTracks >= chipValue
        }

        val dynamicChip = binding.chipGroupQuestions.findViewById<Chip>(R.id.chipDynamicQuestions)
        if (dynamicChip != null) {
            dynamicChip.text = totalTracks.toString()
            dynamicChip.visibility =
                if (totalTracks > 0 && !chips.contains(totalTracks)) View.VISIBLE else View.GONE
        }
    }


    private fun setupCreateQuizButton() {
        binding.buttonCreateQuiz.setOnClickListener {
            val quizName = binding.quizNameInput.text.toString()
            val selectedQuestionsChip =
                binding.chipGroupQuestions.findViewById<Chip>(binding.chipGroupQuestions.checkedChipId)
            val selectedTimeChip =
                binding.chipGroupTimeLimit.findViewById<Chip>(binding.chipGroupTimeLimit.checkedChipId)

            if (quizName.isBlank() || selectedQuestionsChip == null || selectedTimeChip == null) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val timeLimit = selectedTimeChip.text.toString().toLong()
            val newQuiz = Quiz(
                name = quizName,
                questions = generateQuestions(selectedQuestionsChip.text.toString().toInt()),
                timeLimit = timeLimit,
                associatedPlaylists = selectedPlaylists.toList()
            )

            viewModel.addQuiz(newQuiz)
            Toast.makeText(requireContext(), "Quiz Created!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun generateQuestions(questionCount: Int): List<Question> {
        val tracks = selectedPlaylists.flatMap { it.tracks }.shuffled()

        return tracks.take(questionCount).map { track ->
            Question(
                questionText = "What is the name of this track?",
                correctAnswer = track.track_title,
                options = generateOptions(track.track_title, tracks),
                trackPreview = track.preview
            )
        }
    }

    private fun generateOptions(correctAnswer: String, tracks: List<Track>): List<String> {
        val incorrectOptions =
            tracks.map { it.track_title }.filter { it != correctAnswer }.shuffled().take(2)

        return (incorrectOptions + correctAnswer).shuffled()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}