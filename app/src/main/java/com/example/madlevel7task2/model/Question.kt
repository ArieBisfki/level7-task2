package com.example.madlevel7task2.model

data class Question(
    val question: String,
    val choices: List<String>,
    val correctAnswerOrdinal: Int
) {
    val correctAnswer: String by lazy { choices[correctAnswerOrdinal] }

    init {
        validate()
    }

    private fun validate() {
        val choicesAreInvalid = choices.size != CHOICE_COUNT
        val maxCorrectAnswerOrdinal = CHOICE_COUNT - 1
        val correctAnswerOrdinalIsInvalid = 0 <= correctAnswerOrdinal && correctAnswerOrdinal <= maxCorrectAnswerOrdinal
        if (choicesAreInvalid || correctAnswerOrdinalIsInvalid) {
            throw InvalidQuestionException()
        }
    }

    inner class InvalidQuestionException : Exception("Question contains invalid data.") {
    }

    companion object {
        private const val CHOICE_COUNT = 3
    }
}