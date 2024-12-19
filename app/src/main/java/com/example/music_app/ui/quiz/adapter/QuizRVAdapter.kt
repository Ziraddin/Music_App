package com.example.music_app.ui.quiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.databinding.QuizItemBinding
import com.example.music_app.databinding.QuizOptionsBottomSheetBinding
import com.example.music_app.ui.quiz.data.Quiz
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Locale

class QuizRVAdapter(
    private var quizzes: List<Quiz>,
    private val onQuizClick: (Quiz) -> Unit,
    private val onOptionSelected: (Quiz, String) -> Unit,
) : RecyclerView.Adapter<QuizRVAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(private val binding: QuizItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.quizName.text = quiz.name
            binding.questionCount.text =
                String.format(Locale.ROOT, "Questions: %d", quiz.questions.size)


            binding.root.setOnClickListener {
                onQuizClick(quiz)
            }


            binding.quizOptions.setOnClickListener {
                showBottomSheet(binding.root.context, quiz)
            }
        }
    }

    private fun showBottomSheet(context: Context, quiz: Quiz) {
        val bottomSheet = BottomSheetDialog(context)
        val binding = QuizOptionsBottomSheetBinding.inflate(LayoutInflater.from(context))
        bottomSheet.setContentView(binding.root)

        binding.optionEdit.setOnClickListener {
            bottomSheet.dismiss()
            onOptionSelected(quiz, "Edit")
        }

        binding.optionDelete.setOnClickListener {
            bottomSheet.dismiss()
            onOptionSelected(quiz, "Delete")
        }

        bottomSheet.show()
    }

    fun updateData(newData: List<Quiz>) {
        quizzes = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount(): Int = quizzes.size

}