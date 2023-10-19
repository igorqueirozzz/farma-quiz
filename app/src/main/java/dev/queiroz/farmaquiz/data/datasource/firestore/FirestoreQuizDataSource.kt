package dev.queiroz.farmaquiz.data.datasource.firestore

interface FirestoreQuizDataSource {
    fun updateCategoriesWithFirestoreData(
        onUpdateFinished: (Boolean) -> Unit
    )

    fun updateQuestionsWithFirestoreData(
        onUpdateFinished: (Boolean) -> Unit
    )

    fun updateAnswersWithFirestoreData(
        onUpdateFinished: (Boolean) -> Unit
    )
}