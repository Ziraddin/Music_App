package com.example.music_app.ui.details.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.databinding.FragmentQuizDetailsBinding
import com.example.music_app.ui.main.MainActivity
import com.example.music_app.ui.playlist.adapter.PlaylistRVAdapter
import com.example.music_app.ui.quiz.Quiz
import com.example.music_app.viewmodel.PlaylistViewModel
import com.example.music_app.viewmodel.QuizViewModel

class QuizDetailsFragment : Fragment() {

    private var _binding: FragmentQuizDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: QuizDetailsFragmentArgs by navArgs()
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var rvAdapter: PlaylistRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizDetailsBinding.inflate(inflater, container, false)
        quizViewModel = (activity as MainActivity).quizViewModel
        playlistViewModel = (activity as MainActivity).playlistViewModel
        val quiz: Quiz = args.quiz
        rvAdapter = PlaylistRVAdapter(quiz.associatedPlaylists)

        bindData(quiz)
        navBack()
        setupPlaylistsRecyclerView()
        launchQuiz()

        return binding.root
    }


    private fun bindData(quiz: Quiz) {
        binding.toolbar.title = quiz.name
    }

    private fun launchQuiz() {
        binding.launchQuizButton.setOnClickListener {
            val action =
                QuizDetailsFragmentDirections.actionQuizDetailsFragmentToQuizChallangeFragment(args.quiz)
            findNavController().navigate(action)
        }
    }

    private fun navBack() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupPlaylistsRecyclerView() {
        binding.recyclerViewPlaylists.adapter = rvAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}