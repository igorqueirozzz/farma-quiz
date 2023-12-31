@file:OptIn(ExperimentalFoundationApi::class)

package dev.queiroz.farmaquiz.ui.screen.quizgame

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenFinished
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenGaming
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenLoading
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.ui.components.QuizGameAppBar
import dev.queiroz.farmaquiz.ui.components.QuizGameContent
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    categoryId: String?,
    onNavigateBack: () -> Unit,
    state: QuizGameState,
    onLoadQuestionByCategory: (String) -> Unit,
    onLoadRandomGaming: () -> Unit,
    onSelectAnswer: (Answer?, Question?) -> Unit,
    onFinishGame: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isComposableStarting by remember {
        mutableStateOf(true)
    }

    if (state is QuizGameState.Loading && isComposableStarting) {
        LaunchedEffect(key1 = Unit) {
            if (categoryId.isNullOrBlank()) {
                onLoadRandomGaming()
            } else {
                onLoadQuestionByCategory(categoryId)
            }
            isComposableStarting = false
        }
    }

    when (state) {
        is QuizGameState.Loading -> Box(
            modifier = modifier
                .fillMaxSize()
                .testTag(quizScreenLoading),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is QuizGameState.Finished -> FinishGameScreen(
            modifier = modifier.testTag(quizScreenFinished),
            state = state,
            onContinueClick = {
                onNavigateBack()
            })

        else -> {
            val questionsWithAnswers =
                if (state is QuizGameState.Gaming) state.questions else (state as QuizGameState.RandomGaming).questions
            val selectedAnswer =
                if (state is QuizGameState.Gaming) state.selectedAnswer else (state as QuizGameState.RandomGaming).selectedAnswer
            QuizGame(
                modifier = modifier,
                category = if (state is QuizGameState.Gaming) state.category else null,
                questionsWithAnswers = questionsWithAnswers,
                selectedAnswer = selectedAnswer,
                onSelectAnswer = { answer, question ->
                    onSelectAnswer(answer, question)
                },
                onFinishGame = { onFinishGame() },
                onExitGame = {
                    onNavigateBack()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizGame(
    category: Category?,
    questionsWithAnswers: List<QuestionWithAnswers>,
    onSelectAnswer: (Answer?, Question?) -> Unit,
    modifier: Modifier = Modifier,
    selectedAnswer: Answer? = null,
    onFinishGame: () -> Unit,
    onExitGame: () -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState {
        questionsWithAnswers.size
    }

    val coroutineScope = rememberCoroutineScope()
    var showExplicationDialog = remember { mutableStateOf(false) }
    var explicationText by remember {
        mutableStateOf("")
    }
    var showExitGameDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitGameDialog = !showExitGameDialog
    }

    Scaffold(
        modifier = modifier.testTag(quizScreenGaming),
        topBar = {
            QuizGameAppBar(
                categoryName = category?.name ?: stringResource(R.string.miscellaneous),
                currentQuestionIndex = (pagerState.currentPage + 1),
                totalOfQuestions = pagerState.pageCount,
                onBackClick = { showExitGameDialog = true },
                onSkipClick = {
                    onSelectAnswer(null, null)
                    if (pagerState.canScrollForward) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    } else {
                        onFinishGame()
                    }
                })
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {

                    val reportErrorIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("igorqueirozdev@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Erro questão ID: ${questionsWithAnswers[pagerState.currentPage].question.id}")
                        putExtra(Intent.EXTRA_TEXT, "*** Descreva o erro abaixo ***")
                    }
                    context.startActivity(reportErrorIntent)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Report,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.report_error),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (selectedAnswer != null) {
                    Button(modifier = Modifier.height(48.dp), onClick = {
                        onSelectAnswer(null, null)
                        if (pagerState.canScrollForward) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                        } else {
                            onFinishGame()
                        }
                    }) {
                        Text(text = stringResource(R.string.next))
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
    ) { innerPadding ->

        when {
            showExplicationDialog.value -> {
                val containerColor =
                    if (selectedAnswer?.isCorrect == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                val contentColor = Color.White
                QuizDialog(
                    title = stringResource(id = R.string.explication),
                    text = explicationText,
                    onDismiss = { showExplicationDialog.value = false },
                    onConfirm = { showExplicationDialog.value = false },
                    containerColor = containerColor,
                    contentColor = contentColor,
                    showCancelButton = false
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            if (questionsWithAnswers.isNotEmpty()) {
                val question = questionsWithAnswers[it]


                if (showExitGameDialog) {

                    QuizDialog(
                        title = "Aviso",
                        text = "Deseja sair do jogo? \nTodo o progresso será perdido.",
                        onDismiss = { showExitGameDialog = false },
                        onConfirm = {
                            onExitGame()
                        })

                }

                QuizGameContent(
                    questionWithAnswers = question,
                    selectedAnswer = selectedAnswer,
                    onItemClick = { answer ->
                        if (selectedAnswer == null) {
                            onSelectAnswer(answer, question.question)
                        }
                    },
                    onSeeExplicationClick = {
                        explicationText = question.question.explication
                        showExplicationDialog.value = true

                    },
                    modifier = modifier
                        .padding(
                            horizontal = 16.dp
                        )
                )
            }

        }
    }
}

@Composable
fun FinishGameScreen(
    state: QuizGameState.Finished,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startStarAnimation by remember { mutableStateOf(false) }
    var showInfoCard by remember { mutableStateOf(false) }
    var showContinueButton by remember { mutableStateOf(false) }
    val starAnimation by animateDpAsState(
        targetValue = if (startStarAnimation) 200.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "starSizeAnim"
    )

    LaunchedEffect(key1 = Unit) {
        delay(100)
        startStarAnimation = true
        delay(500)
        showInfoCard = true
        delay(500)
        showContinueButton = true
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {


        Icon(
            modifier = Modifier.size(starAnimation),
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )



        AnimatedVisibility(
            visible = showInfoCard,
            enter = expandVertically(),
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, style = MaterialTheme.typography.displayMedium)
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Perguntas respondidas: ${state.answeredQuestions} de ${state.totalOfQuestions}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Pontuação: ${state.score}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        AnimatedVisibility(visible = showContinueButton) {
            ElevatedButton(onClick = onContinueClick) {
                Text(
                    text = stringResource(R.string.doContinue),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

@Composable
fun QuizDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    showCancelButton: Boolean? = true,
    dialogProperties: DialogProperties? = null,
    centerContent: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        properties = dialogProperties ?: DialogProperties(),
        modifier = modifier,
        dismissButton = {
            if (showCancelButton == true) {
                TextButton(onClick = onDismiss, modifier = Modifier.padding(end = 16.dp)) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            ElevatedButton(onClick = onConfirm, modifier = Modifier.padding(start = 16.dp)) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        icon = {
            if (imageVector != null)
                Icon(
                    imageVector = imageVector,
                    contentDescription = null
                )
        },
        title = { Text(text = title) },
        text = {
            Column {
                Text(text = text)
                centerContent?.invoke()
            }
        },
        containerColor = containerColor ?: MaterialTheme.colorScheme.surface,
        iconContentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        titleContentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        textContentColor = contentColor ?: MaterialTheme.colorScheme.onSurface
    )
}


@Composable
@Preview
fun QuizScreenPreview() {
    FarmaQuizTheme {
        QuizScreen(
            categoryId = CategoriesDummy.categories.first().id,
            onNavigateBack = {},
            state = QuizGameState.Gaming(
                category = CategoriesDummy.categories.first(),
                questions = CategoriesDummy.questions
            ),
            onLoadQuestionByCategory = {},
            onLoadRandomGaming = {},
            onSelectAnswer = { _, _ -> },
            onFinishGame = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun FinishedGamePreview() {
    FarmaQuizTheme {
        FinishGameScreen(
            state = QuizGameState.Finished(
                message = "Test",
                answeredQuestions = 2,
                totalOfQuestions = 5,
                score = 100
            ),
            onContinueClick = {}
        )
    }
}
