package dev.queiroz.farmaquiz.data.datasource.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.queiroz.farmaquiz.data.datasource.firestore.AnswerFields.IS_CORRECT
import dev.queiroz.farmaquiz.data.datasource.firestore.AnswerFields.QUESTION_ID
import dev.queiroz.farmaquiz.data.datasource.firestore.AnswerFields.TEXT
import dev.queiroz.farmaquiz.data.datasource.firestore.CategoryFields.IMAGE_NAME
import dev.queiroz.farmaquiz.data.datasource.firestore.CategoryFields.NAME
import dev.queiroz.farmaquiz.data.datasource.firestore.Collections.ANSWERS
import dev.queiroz.farmaquiz.data.datasource.firestore.Collections.CATEGORIES
import dev.queiroz.farmaquiz.data.datasource.firestore.Collections.QUESTIONS
import dev.queiroz.farmaquiz.data.datasource.firestore.QuestionFields.CATEGORY_ID
import dev.queiroz.farmaquiz.data.datasource.firestore.QuestionFields.DIFFICULT
import dev.queiroz.farmaquiz.data.datasource.firestore.QuestionFields.EXPLICATION
import dev.queiroz.farmaquiz.data.datasource.firestore.QuestionFields.QUESTION
import dev.queiroz.farmaquiz.data.repository.AnswerRepository
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.enum.Difficult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FirestoreQuizDataSourceImpl(
    private val categoryRepository: CategoryRepository,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository
) : FirestoreQuizDataSource {

    private val db = Firebase.firestore

    override fun updateCategoriesWithFirestoreData(
        onUpdateFinished: (Boolean) -> Unit
    ) {
        db.collection(CATEGORIES)
            .get()
            .addOnSuccessListener { query ->
                val categories =
                    query.documents.filter { !it.metadata.isFromCache }.map { documentSnapshot ->
                        val id: String = documentSnapshot.id
                        val name: String = documentSnapshot.getString(NAME)!!
                        val imageName: String = documentSnapshot.getString(IMAGE_NAME)!!
                        Category(id = id, name = name, imageName = imageName)
                    }
                if (categories.isNotEmpty()) {
                    onUpdateFinished(true)
                    GlobalScope.launch(Dispatchers.IO) {
                        categoryRepository.insertAll(categories = categories)
                    }
                } else {
                    onUpdateFinished(false)
                }

            }.addOnFailureListener {
                onUpdateFinished(false)
            }
    }

    override fun updateQuestionsWithFirestoreData(onUpdateFinished: (Boolean) -> Unit) {
        db.collection(QUESTIONS)
            .get()
            .addOnSuccessListener { query ->
                val questions =
                    query.documents.filter { !it.metadata.isFromCache }.map { documentSnapshot ->
                        val id = documentSnapshot.id
                        val categoryId = documentSnapshot.getString(CATEGORY_ID)!!
                        val difficult = Difficult.valueOf(documentSnapshot.getString(DIFFICULT)!!)
                        val explication = documentSnapshot.getString(EXPLICATION)!!
                        val questionText = documentSnapshot.getString(QUESTION)!!
                        Question(
                            id = id,
                            question = questionText,
                            categoryId = categoryId,
                            explication = explication,
                            difficult = difficult
                        )
                    }

                if (questions.isNotEmpty()){
                    onUpdateFinished(true)
                   GlobalScope.launch(Dispatchers.IO) {
                       questionRepository.insertAll(questions = questions)
                   }
                }else {
                    onUpdateFinished(false)
                }
            }.addOnFailureListener {
                onUpdateFinished(false)
            }
    }

    override fun updateAnswersWithFirestoreData(onUpdateFinished: (Boolean) -> Unit) {
        db.collection(ANSWERS)
            .get()
            .addOnSuccessListener { query ->
                val answers = query.documents.filter { !it.metadata.isFromCache }.map { documentSnapshot ->
                    val id = documentSnapshot.id
                    val questionId = documentSnapshot.getString(QUESTION_ID)!!
                    val isCorrect = documentSnapshot.getBoolean(IS_CORRECT)!!
                    val text = documentSnapshot.getString(TEXT)!!
                    Answer(
                        id = id,
                        questionId = questionId,
                        isCorrect = isCorrect,
                        text = text
                    )
                }

                if(answers.isNotEmpty()){
                    GlobalScope.launch(Dispatchers.IO) {
                        answerRepository.insertAll(answers = answers)
                    }
                }
            }
    }


}