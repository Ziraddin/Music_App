package com.example.music_app.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.music_app.databinding.FragmentQuizChallangeBinding
import com.example.music_app.databinding.ItemQuestionCardBinding
import com.example.music_app.ui.details.track.MusicController
import com.example.music_app.ui.main.MainActivity
import com.example.music_app.ui.quiz.adapter.QuestionAdapter
import com.example.music_app.viewmodel.QuizViewModel


class QuizChallangeFragment : Fragment() {

    private var _binding: FragmentQuizChallangeBinding? = null
    private val binding get() = _binding!!
    private val args: QuizChallangeFragmentArgs by navArgs()
    private lateinit var viewPager: ViewPager2
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizChallangeBinding.inflate(inflater, container, false)
        quizViewModel = (activity as MainActivity).quizViewModel
        val quiz: Quiz = args.quiz
        viewPager = binding.viewPager
        questionAdapter = QuestionAdapter(quiz.questions, onNextQuestion = {
            viewPager.currentItem += 1
            if (viewPager.currentItem + 1 == quiz.questions.size) {
                val binding = ItemQuestionCardBinding.inflate(layoutInflater)
                binding.actionButton.text = "Finish"
                findNavController().navigateUp()
            }
        })

        bindData(quiz)
        setupViewPager(quiz)
        navBack()

        return binding.root
    }

    private fun bindData(quiz: Quiz) {
        binding.toolbar.title = quiz.name
    }

    private fun navBack() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewPager(quiz: Quiz) {
        viewPager.adapter = questionAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val question = quiz.questions[position]
                playMusicPreview(question.trackPreview)
            }
        })
    }

    private fun playMusicPreview(trackPreview: String) {
        MusicController.setMediaItem(requireContext(), trackPreview)
        MusicController.playPreview()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MusicController.stop()
        _binding = null
    }
}
