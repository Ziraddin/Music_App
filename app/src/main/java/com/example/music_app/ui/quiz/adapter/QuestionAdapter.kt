package com.example.music_app.ui.quiz.adapter

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.databinding.ItemQuestionCardBinding
import com.example.music_app.ui.quiz.data.Question

class QuestionAdapter(
    private val questions: List<Question>,
    private val onNextQuestion: () -> Unit,
    private val timeLimit: Long,
    private val onFinishQuiz: () -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var selectedOption: String? = null
        private var countDownTimer: CountDownTimer? = null
        private lateinit var optionViews: Map<String, RadioButton>

        fun bind(question: Question) {
            binding.questionText.text = question.questionText
            binding.option1.text = question.options[0]
            binding.option2.text = question.options[1]
            binding.option3.text = question.options[2]

            optionViews = mapOf(
                question.options[0] to binding.option1,
                question.options[1] to binding.option2,
                question.options[2] to binding.option3
            )

            binding.actionButton.text = "Check"
            binding.actionButton.isEnabled = false
            var flag: Boolean = false

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
                if (!flag) {
                    if (selectedOption == question.correctAnswer) {
                        optionViews[selectedOption]?.setTextColor(
                            binding.root.context.getColor(android.R.color.holo_green_light)
                        )
                    } else {
                        optionViews[selectedOption]?.setTextColor(
                            binding.root.context.getColor(android.R.color.holo_red_light)
                        )
                        optionViews[question.correctAnswer]?.setTextColor(
                            binding.root.context.getColor(android.R.color.holo_green_light)
                        )
                    }
                    if (bindingAdapterPosition == questions.size - 1) {
                        binding.actionButton.text = "Finish"
                        binding.actionButton.setOnClickListener {
                            onFinishQuiz()
                        }
                    } else {
                        binding.actionButton.text = "Next"
                        binding.optionsGroup.forEach { radioButton ->
                            (radioButton as? RadioButton)?.isEnabled = false
                        }
                    }
                    stopTimer()
                    flag = true
                } else {
                    onNextQuestion()
                    binding.optionsGroup.forEach { radioButton ->
                        (radioButton as? RadioButton)?.isEnabled = true
                    }
                }
            }
            startTimer(timeLimit)
        }

        private fun startTimer(timeLimit: Long) {
            countDownTimer?.cancel()

            countDownTimer = object : CountDownTimer(timeLimit, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val progress = (millisUntilFinished.toFloat() / timeLimit.toFloat()) * 100
                    binding.progressIndicator.setProgress(progress.toInt(), true)
                    binding.timeLimit.text = (millisUntilFinished / 1000).toString()
                }

                override fun onFinish() {
                    binding.timeLimit.text = "0"
                    binding.progressIndicator.setProgress(0, true)
                }
            }
            countDownTimer?.start()
        }

        fun stopTimer() {
            countDownTimer?.cancel()
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

    override fun onViewRecycled(holder: QuestionViewHolder) {
        super.onViewRecycled(holder)
        holder.stopTimer()
    }

    override fun getItemCount() = questions.size
}


