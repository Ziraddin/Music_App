package com.example.music_app.ui.quiz

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.music_app.databinding.DialogConfirmDeleteBinding
import com.example.music_app.databinding.DialogUpdatePlaylistBinding
import com.example.music_app.databinding.FragmentQuizBinding
import com.example.music_app.ui.quiz.adapter.QuizRVAdapter
import com.example.music_app.viewmodel.QuizState
import com.example.music_app.viewmodel.QuizViewModel


class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: QuizRVAdapter
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)

        rvAdapter = QuizRVAdapter(
            emptyList(),
            onQuizClick = ::onQuizClick,
            onOptionSelected = ::onOptionSelected,
        )

        setGridRv()
        createPlaylist()
        viewModel.getQuizzes()
        observeQuizState()

        return binding.root
    }


    private fun observeQuizState() {
        viewModel.quizState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is QuizState.Success -> {
                    rvAdapter.updateData(state.result)
                }

                is QuizState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        })
    }

    private fun createPlaylist() {
        binding.createQuizButton.setOnClickListener {
            val navController = findNavController()
            val action = QuizFragmentDirections.actionQuizFragmentToCreateQuizFragment()
            navController.navigate(action)
        }
    }

    //Recycler View Methods
    private fun setGridRv() {
        binding.recyclerViewQuizzes.adapter = rvAdapter
    }

    private fun onQuizClick(quiz: Quiz) {
        val navController = findNavController()
        val action = QuizFragmentDirections.actionQuizFragmentToQuizDetailsFragment(quiz)
        navController.navigate(action)
    }

    private fun onOptionSelected(quiz: Quiz, action: String) {
        when (action) {
            "Edit" -> {
                showUpdateDialog(quiz)
            }

            "Delete" -> {
                confirmDelete(quiz)
            }
        }
    }

    //Dialogs
    private fun confirmDelete(quiz: Quiz) {
        val dialog = Dialog(requireContext())
        val binding = DialogConfirmDeleteBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val confirmButton = binding.btnDelete
        val cancelButton = binding.btnCancel
        binding.tvDeleteMessage.text = "Are you sure you want to\ndelete this Quiz?"

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        confirmButton.setOnClickListener {
            viewModel.removeQuiz(quiz)
            Toast.makeText(requireContext(), "Quiz deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showUpdateDialog(quiz: Quiz) {
        val dialog = Dialog(requireContext())
        val binding = DialogUpdatePlaylistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val quizNameEditText = binding.editPlaylistName
        val updateButton = binding.btnUpdate
        val cancelButton = binding.btnCancel

        quizNameEditText.setText(quiz.name)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        updateButton.setOnClickListener {
            val newName = quizNameEditText.text.toString().trim()
            if (newName.isEmpty()) {
                quizNameEditText.error = "Name is required"
            } else {
                quiz.name = newName
                viewModel.updateQuiz(quiz)
                Toast.makeText(requireContext(), "Quiz updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}