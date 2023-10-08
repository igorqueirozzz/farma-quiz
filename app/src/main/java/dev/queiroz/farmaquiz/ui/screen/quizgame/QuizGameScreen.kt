@file:OptIn(ExperimentalFoundationApi::class)

package dev.queiroz.farmaquiz.ui.screen.quizgame

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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenFinished
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenGaming
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenLoading
import dev.queiroz.farmaquiz.data.CategoriesDummy
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.ui.components.QuizAnswerList
import dev.queiroz.farmaquiz.ui.components.QuizGameAppBar
import dev.queiroz.farmaquiz.ui.components.QuizQuestionContent
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    category: Category,
    onNavigateBack: () -> Unit,
    state: QuizGameState,
    onLoadQuestionByCategory: (Category) -> Unit,
    onSelectAnswer: (Answer?, Question?) -> Unit,
    onFinishGame: () -> Unit,
    onResetGame: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (state is QuizGameState.Loading) {
        LaunchedEffect(key1 = Unit){
            onLoadQuestionByCategory(category)
        }
    }

    when (state) {
        is QuizGameState.Loading -> Box(
            modifier = modifier
                .fillMaxSize()
                .testTag(quizScreenLoading),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is QuizGameState.Gaming -> {
            QuizGame(
                modifier = modifier,
                category = state.category,
                questions = state.questions,
                selectedAnswer = state.selectedAnswer,
                onSelectAnswer = { answer, question ->
                    onSelectAnswer(answer, question)
                },
                onFinishGame = { onFinishGame() }
            )
        }

        is QuizGameState.Finished -> FinishGameScreen(
            modifier = modifier.testTag(quizScreenFinished),
            state = state,
            onContinueClick = {
                onNavigateBack()
                onResetGame()
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuizGame(
    category: Category,
    questions: List<Question>,
    onSelectAnswer: (Answer?, Question?) -> Unit,
    modifier: Modifier = Modifier,
    selectedAnswer: Answer? = null,
    onFinishGame: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState {
        questions.size
    }

    val coroutineScope = rememberCoroutineScope()
    var showExplicationDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.testTag(quizScreenGaming), topBar = {
        QuizGameAppBar(categoryName = category.name,
            currentQuestionIndex = (pagerState.currentPage + 1),
            totalOfQuestions = pagerState.pageCount,
            onBackClick = { /*TODO*/ },
            onSkipClick = { /*TODO*/ })
    }, floatingActionButton = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            TextButton(onClick = { /*TODO*/ }) {
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
    }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.padding(innerPadding)
        ) {
            Column {
                val question = questions[it]

                if (showExplicationDialog) {
                    ExplicationDialog(
                        explication = question.explication,
                        isCorrectAnswer = selectedAnswer!!.isCorrect,
                        onDismiss = { showExplicationDialog = false },
                        onConfirm = { showExplicationDialog = false },
                    )
                }

                QuizQuestionContent(
                    question = question,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                )

                QuizAnswerList(
                    answers = question.answers,
                    selectedAnswer = selectedAnswer,
                    onItemClick = { answer ->
                        if (selectedAnswer == null) {
                            onSelectAnswer(answer, question)
                        }
                    },
                    onSeeExplicationClick = { showExplicationDialog = true },
                    modifier = modifier
                        .padding(
                            horizontal = 32.dp
                        )
                        .weight(1f)
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
            contentDescription = null
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
fun ExplicationDialog(
    explication: String, isCorrectAnswer: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    val containerColor =
        if (isCorrectAnswer) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
    val contentColor =
        if (isCorrectAnswer) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.ok))
            }
        },
        icon = {
            Icon(
                imageVector = if (isCorrectAnswer) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                contentDescription = null
            )
        },
        title = { Text(text = stringResource(R.string.explication)) },
        text = { Text(text = explication) },
        containerColor = containerColor,
        iconContentColor = contentColor,
        titleContentColor = contentColor,
        textContentColor = contentColor
    )
}


@Composable
@Preview
fun QuizScreenPreview() {
    FarmaQuizTheme {
        QuizScreen(
            category = CategoriesDummy.categories.first(),
            onNavigateBack = {},
            state = QuizGameState.Loading,
            onFinishGame = {},
            onLoadQuestionByCategory = {},
            onResetGame = {},
            onSelectAnswer = { _, _ -> }
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
