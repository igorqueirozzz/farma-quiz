package dev.queiroz.farmaquiz.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags
import dev.queiroz.farmaquiz.constants.TestTags.answerOptionCard
import dev.queiroz.farmaquiz.constants.TestTags.answersOptionsList
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.launch

@Composable
fun QuizGameContent(
    questionWithAnswers: QuestionWithAnswers,
    onItemClick: (Answer) -> Unit,
    onSeeExplicationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedAnswer: Answer? = null,
) {
    val scope = rememberCoroutineScope()
    val iconColors = IconButtonDefaults.outlinedIconButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    val listState = rememberLazyListState()
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    val correctAnswer = questionWithAnswers.answers.find { it.isCorrect }
    val explication = "${stringResource(id = R.string.correct_answer)}: ${getLetterOption(questionWithAnswers.answers.indexOf(correctAnswer))}\n\n${questionWithAnswers.question.explication}"
    Box(contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = modifier
                .selectableGroup()
                .testTag(answersOptionsList),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    val hasImage = !questionWithAnswers.question.imageResource.isNullOrEmpty()
                    if (hasImage) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .align(Alignment.CenterHorizontally)
                                .testTag(tag = TestTags.imageView),
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(questionWithAnswers.question.imageResource)
                                .crossfade(true)
                                .build(),
                            loading = {
                                Box(contentAlignment = Alignment.Center){
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                Box{
                                    Card(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(R.string.msg_failure_load_image),
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            },
                            contentDescription = stringResource(R.string.imageContent),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Text(
                        text = questionWithAnswers.question.question,
                        textAlign = TextAlign.Start,
                        style = if (hasImage) MaterialTheme.typography.titleMedium else MaterialTheme.typography.displayMedium
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
            items(items = questionWithAnswers.answers) { answer ->
                QuizAnswerItem(
                    answer = answer,
                    answerIndex = questionWithAnswers.answers.indexOf(answer),
                    onItemClick = onItemClick,
                    isSelected = answer == selectedAnswer,
                    onSeeExplicationClick = { onSeeExplicationClick(explication) }
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = listState.canScrollForward
        ) {
            IconButton(
                colors = iconColors,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(questionWithAnswers.answers.size)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowCircleDown,
                    contentDescription = null,
                )
            }
        }

    }
}

@Composable
fun QuizAnswerItem(
    answer: Answer,
    answerIndex: Int,
    isSelected: Boolean,
    onItemClick: (Answer) -> Unit,
    onSeeExplicationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected && answer.isCorrect) MaterialTheme.colorScheme.primaryContainer
        else if (isSelected && !answer.isCorrect) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.surface,
        label = "",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag(answerOptionCard)
            .selectable(selected = isSelected, onClick = { onItemClick(answer) })
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.large
            ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = "${getLetterOption(answerIndex)})",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = answer.text,
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            if (isSelected) {
                Icon(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    imageVector = if (answer.isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                    contentDescription = null
                )
            }
        }
        if (isSelected) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSeeExplicationClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.see_explication))
                Icon(imageVector = Icons.Rounded.Visibility, contentDescription = null)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun QuizGameContentPreview() {
    FarmaQuizTheme {
        Column {
            QuizGameContent(
                onItemClick = {},
                questionWithAnswers = CategoriesDummy.questions.first(),
                onSeeExplicationClick = {}
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun QuizAnswerItemPreview() {
    FarmaQuizTheme {
        Column {
            QuizAnswerItem(answer = CategoriesDummy.questions.first().answers.first(),
                onItemClick = {},
                answerIndex = 0,
                isSelected = false,
                onSeeExplicationClick = {})
        }
    }
}

fun getLetterOption(index: Int): String {
    val options = listOf("A", "B", "C", "D")
    return options[index]
}