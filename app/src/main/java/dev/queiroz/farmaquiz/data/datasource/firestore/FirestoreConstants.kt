package dev.queiroz.farmaquiz.data.datasource.firestore

object Collections {
    const val CATEGORIES = "categories"
    const val QUESTIONS = "questions"
    const val ANSWERS = "answers"
}

object CategoryFields {
    const val NAME = "name"
    const val IMAGE_NAME = "imageName"
}

object QuestionFields {
    const val QUESTION = "question"
    const val CATEGORY_ID = "categoryId"
    const val DIFFICULT = "difficult"
    const val EXPLICATION = "explication"
    const val IMAGE_RESOURCE = "imageResource"
}

object AnswerFields {
    const val IS_CORRECT = "isCorrect"
    const val QUESTION_ID = "questionId"
    const val TEXT = "text"
}