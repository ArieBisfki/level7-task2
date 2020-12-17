package com.example.madlevel7task2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task2.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuestionRepository {
    private var firestore = FirebaseFirestore.getInstance()
    private var questionsCollection = firestore.collection("questions")

    private val _questionsList = MutableLiveData<List<Question>>()

    val questionsList: LiveData<List<Question>>
        get() = _questionsList

    suspend fun fetchQuestionsList() {
        try {
            withTimeout(5000) {
                val result = questionsCollection.get().await()
                val mappedToQuestionsList = result.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.data?.let { it -> Question.fromMap(it) }
                }
                _questionsList.value = mappedToQuestionsList
            }
        } catch (e: Exception) {
            throw RetrievalError()
        }
    }

    inner class RetrievalError : Exception("Failed to retrieve question.")
}