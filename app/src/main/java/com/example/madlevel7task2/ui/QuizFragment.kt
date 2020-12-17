package com.example.madlevel7task2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentQuizBinding
import com.example.madlevel7task2.vm.QuestionsViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuizFragment : Fragment() {

    private val vm: QuestionsViewModel by activityViewModels()
    private lateinit var binding: FragmentQuizBinding

    fun RadioGroup.getCheckedRadioButtonIndex() =
        indexOfChild(findViewById(checkedRadioButtonId))

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeQuestion()
    }

    private fun initViews() {
        binding.btnConfirm.setOnClickListener(::handleBtnConfirmClick)
    }

    private fun observeQuestion() {
        vm.currentQuestion.observe(viewLifecycleOwner) { question ->
            binding.tvQuizCount.text = getString(R.string.fragment_quiz_quiz_count, vm.currentQuestionIndex + 1, vm.questionsListSize)
            binding.tvQuestion.text = question?.question
            val radioButtonsIterator = binding.radioGroup.children.iterator().withIndex()
            radioButtonsIterator.forEach { (index, radioButton) ->
                (radioButton as RadioButton).text = question?.choicesList?.get(index)
            }
        }
    }

    private fun handleBtnConfirmClick(view: View) {
        val answerIndex = binding.radioGroup.getCheckedRadioButtonIndex()
        if(vm.answerIsCorrect(answerIndex)) {
            handleAnswerCorrect()
        } else {
            handleAnswerIncorrect()
        }
    }

    private fun handleAnswerCorrect() {
        val nextQuestionWasSet = vm.setNextQuestion()

        if (!nextQuestionWasSet) {
            findNavController().popBackStack()
        }
    }

    private fun handleAnswerIncorrect() {
        Snackbar
            .make(binding.root, R.string.fragment_quiz_answer_incorrect, Snackbar.LENGTH_SHORT)
            .show()
    }
}