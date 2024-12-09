package com.example.music_app.ui.details.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.music_app.databinding.FragmentQuizDetailsBinding
import com.example.music_app.ui.quiz.Quiz
import com.example.music_app.viewmodel.QuizViewModel

class QuizDetailsFragment : Fragment() {

    private var _binding: FragmentQuizDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: QuizDetailsFragmentArgs by navArgs()
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizDetailsBinding.inflate(inflater, container, false)

        val quiz: Quiz = args.quiz

        bindData(quiz)

        return binding.root
    }


    private fun bindData(quiz: Quiz) {
        binding.toolbar.title = quiz.name
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}