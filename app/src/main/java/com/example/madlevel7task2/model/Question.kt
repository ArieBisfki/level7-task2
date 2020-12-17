package com.example.madlevel7task2.model

data class Question(
        val question: String,
        val correctAnswerOrdinal: Int,
        private val choice1: String,
        private val choice2: String,
        private val choice3: String
) {
    val correctAnswer: String by lazy {
        choicesList[correctAnswerOrdinal]
    }

    val choicesList: List<String> by lazy {
        listOf(choice1, choice2, choice3)
    }

    init {
        validate()
    }

    private fun validate() {
        val choicesAreInvalid = choicesList.size != CHOICE_COUNT
        val maxCorrectAnswerOrdinal = CHOICE_COUNT - 1
        val correctAnswerOrdinalIsInvalid = correctAnswerOrdinal !in 0..maxCorrectAnswerOrdinal
        if (choicesAreInvalid || correctAnswerOrdinalIsInvalid) {
            throw InvalidQuestionException()
        }
    }

    inner class InvalidQuestionException : Exception("Question contains invalid data.")

    companion object {
        private const val CHOICE_COUNT = 3

        fun fromMap(map: Map<String, Any>) = Question(
            question = map["question"] as String,
            choice1 = map["choice1"] as String,
            choice2 = map["choice2"] as String,
            choice3 = map["choice3"] as String,
            correctAnswerOrdinal = (map["correctAnswerOrdinal"] as Long).toInt()
        )
    }
}