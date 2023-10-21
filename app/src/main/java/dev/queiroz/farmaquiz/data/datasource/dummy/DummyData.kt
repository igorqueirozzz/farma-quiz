package dev.queiroz.farmaquiz.data.datasource.dummy

import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.model.enum.Difficult

object CategoriesDummy {
    val categories = listOf<Category>(
        Category(id = "1", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "2", name = "Test 1 Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "3", name = "test 2 Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "4", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "5", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "6", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "7", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "8", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "9", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "10", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "11", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
        Category(id = "12", name = "Quimica Farmacêutica", imageName = "pic_chemical_demo"),
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
            id = "3", text = "Paracetamol", isCorrect = true, questionId = "2"
        ),
        Answer(
            id = "4",
            text = "Antidepressivo",
            isCorrect = false,
            questionId = "2"
        ),

        Answer(
            id = "5",
            text = "Antibiótico",
            isCorrect = false,
            questionId = "3"
        ),
        Answer(
            id = "6",
            text = "Anti-inflamatório",
            isCorrect = false,
            questionId = "3"
        ),
        Answer(
            id = "7", text = "Paracetamol", isCorrect = true, questionId = "4"
        ),
        Answer(
            id = "8",
            text = "Antidepressivo",
            isCorrect = false,
            questionId = "4"
        ),
        Answer(
            id = "9", text = "Paracetamol", isCorrect = true, questionId = "5"
        ),
        Answer(
            id = "10",
            text = "Antidepressivo",
            isCorrect = false,
            questionId = "5"
        ),

        Answer(
            id = "11", text = "Paracetamol", isCorrect = true, questionId = "6"
        ),
        Answer(
            id = "12",
            text = "Antidepressivo",
            isCorrect = false,
            questionId = "6"
        ),
    )

    val questions = listOf(
        QuestionWithAnswers(
            question = Question(
                id = "1",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),
        QuestionWithAnswers(
            question = Question(
                id = "2",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),
        QuestionWithAnswers(
            question = Question(
                id = "3",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),
        QuestionWithAnswers(
            question = Question(
                id = "4",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),
        QuestionWithAnswers(
            question = Question(
                id = "5",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),
        QuestionWithAnswers(
            question = Question(
                id = "6",
                question = "Qual é a principal função dos antibióticos?",
                categoryId = "first",
                alreadyAnswered = false,
                imageResource = "pic_chemical_demo",
                explication = "Test",
                difficult = Difficult.easy
            ),
            answers = answers.shuffled()
        ),


        )


}