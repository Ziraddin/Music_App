package com.example.music_app.ui.quiz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.databinding.ItemQuestionCardBinding
import com.example.music_app.ui.quiz.Question

class QuestionAdapter(
    private val questions: List<Question>, private val onNextQuestion: () -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var selectedOption: String? = null

        fun bind(question: Question) {
            binding.questionText.text = question.questionText
            binding.option1.text = question.options[0]
            binding.option2.text = question.options[1]
            binding.option3.text = question.options[2]

            binding.actionButton.text = "Check"
            binding.actionButton.isEnabled = false

            binding.optionsGroup.setOnCheckedChangeListener { _, checkedId ->
                selectedOption = when (checkedId) {
                    binding.option1.id -> binding.option1.text.toString()
                    binding.option2.id -> binding.option2.text.toString()
                    binding.option3.id -> binding.option3.text.toString()
                    else -> null
                }
                binding.actionButton.isEnabled = selectedOption != null
            }

            binding.actionButton.setOnClickListener {
                if (selectedOption != null) {
                    binding.actionButton.text = "Next"
                    onNextQuestion()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount() = questions.size
}

