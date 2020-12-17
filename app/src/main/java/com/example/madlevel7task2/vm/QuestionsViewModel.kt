package com.example.madlevel7task2.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madlevel7task2.model.Question
import com.example.madlevel7task2.repository.QuestionRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"

    private val questionRepository = QuestionRepository()

    private val questionsList = questionRepository.questionsList

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?>
        get() = _currentQuestion

    val questionsListSize
        get() = questionsList.value?.size ?: -1

    var currentQuestionIndex = -1
        private set(value) {
            field = value
            _currentQuestion.value = questionsList.value?.get(field)
        }

    fun fetchQuestionsList() =
        viewModelScope.launch {
            try {
                questionRepository.fetchQuestionsList()

                if (questionsList.value.isNullOrEmpty()) {
                    throw EmptyQuestionsListError()
                }

                currentQuestionIndex = 0
            } catch (ex: QuestionRepository.RetrievalError) {
                val errorMsg = "Something went wrong while retrieving question"
                Log.e(TAG, errorMsg)
            }
        }

    inner class EmptyQuestionsListError : Exception("Questions list is empty.")

    fun setNextQuestion(): Boolean {
        // Do not continue if already at last question
        if (currentQuestionIndex == questionsList.value!!.size - 1) {
            return false
        }

        // Increment question
        _currentQuestion.value = questionsList.value!![++currentQuestionIndex]
        return true
    }

    fun answerIsCorrect(answerIndex: Int): Boolean {
        return _currentQuestion.value?.correctAnswerOrdinal == answerIndex
    }
}