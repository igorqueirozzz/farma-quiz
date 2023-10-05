package dev.queiroz.farmaquiz.data

import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.enum.Difficult

object CategoriesDummy {
    val categories = listOf<Category>(
        Category(id = "1", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "2", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "3", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "4", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "5", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "6", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "7", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "8", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "9", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "10", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "11", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
        Category(id = "12", name = "Quimica Farmacêutica", imageId = R.drawable.pic_chemical_demo),
    )

    val answers = listOf(
        Answer(
            id = "1",
            text = "Antibiótico",
            isCorrect = false,
            questionId = "1"
        ),
        Answer(
            id = "2",
            text = "Anti-inflamatório",
            isCorrect = false,
            questionId = "1"
        ),
        Answer(
            id = "3", text = "Paracetamol", isCorrect = true, questionId = "1"
        ),
        Answer(
            id = "4",
            text = "Antidepressivo",
            isCorrect = false,
            questionId = "1"
        ),
    )

    val questions = listOf(
        Question(
            id = "1",
            question = "Qual é a principal função dos antibióticos?",
            answers.shuffled(),
            categoryId = "1",
            alreadyAnswered = false,
            imageResource = R.drawable.ic_medication_demo,
            explication = "Test",
            difficult = Difficult.easy
        ),
        Question(
            id = "2",
            question = "Qual destes medicamentos é um analgésico comum usado para aliviar a dor leve a moderada?",
            answers.shuffled(),
            categoryId = "1",
            alreadyAnswered = false,
            explication = "Test",
            difficult = Difficult.medium
        ),

        Question(
            id = "1",
            question = " O que significa a sigla \"OTC\" quando se trata de medicamentos?",
            answers.shuffled(),
            categoryId = "1",
            alreadyAnswered = false,
            explication = "Test",
            difficult = Difficult.hard
        ),

        Question(
            id = "1",
            question = "Qual destes medicamentos é um analgésico comum usado para aliviar a dor leve a moderada?",
            answers.shuffled(),
            categoryId = "1",
            alreadyAnswered = false,
            explication = "Test",
            difficult = Difficult.easy
        ),

        Question(
            id = "1",
            question = "Qual destes medicamentos é um analgésico comum usado para aliviar a dor leve a moderada?",
            answers.shuffled(),
            categoryId = "1",
            alreadyAnswered = false,
            explication = "Test",
            difficult = Difficult.medium
        ),
    )


}