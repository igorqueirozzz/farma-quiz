package dev.queiroz.farmaquiz.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags.answerOptionCard
import dev.queiroz.farmaquiz.constants.TestTags.answersOptionsList
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.extensions.getDrawableIdentifier
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.launch

@Composable
fun QuizQuestionContent(
    questionWithAnswers: QuestionWithAnswers, modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val iconButtonColor = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    val scrollAnimDuration = 1500
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center
        ) {
            val hasImage = questionWithAnswers.question.imageResource != null
            if (hasImage) {
                Image(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    painter = painterResource(
                        id = LocalContext.current.getDrawableIdentifier(
                            questionWithAnswers.question.imageResource!!
                        )
                    ),
                    contentDescription = stringResource(R.string.imageContent),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = questionWithAnswers.question.question,
                textAlign = TextAlign.Start,
                style = if (hasImage) MaterialTheme.typography.titleMedium else MaterialTheme.typography.displayMedium
            )


        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = scrollState.canScrollForward,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            IconButton(
                colors = iconButtonColor,
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(
                            value = scrollState.maxValue,
                            animationSpec = tween(durationMillis = scrollAnimDuration)
                        )
                    }
                }) {
                Icon(
                    imageVector = Icons.Filled.ArrowCircleDown,
                    contentDescription = null
                )
            }
        }



        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopEnd),
            visible = scrollState.canScrollBackward,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            IconButton(
                colors = iconButtonColor,
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(
                            value = 0,
                            animationSpec = tween(durationMillis = scrollAnimDuration)
                        )
                    }
                }) {
                Icon(
                    imageVector = Icons.Filled.ArrowCircleUp,
                    contentDescription = null
                )
            }
        }


    }
}

@Composable
fun QuizAnswerList(
    answers: List<Answer>,
    selectedAnswer: Answer? = null,
    onItemClick: (Answer) -> Unit,
    onSeeExplicationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .selectableGroup()
            .testTag(answersOptionsList),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = answers) { answer ->
            QuizAnswerItem(
                answer = answer,
                answerIndex = answers.indexOf(answer),
                onItemClick = onItemClick,
                isSelected = answer == selectedAnswer,
                onSeeExplicationClick = onSeeExplicationClick
            )
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
        else MaterialTheme.colorScheme.tertiaryContainer,
        label = "",
    )

    Card(
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else (-1).dp,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .animateContentSize()
            .testTag(answerOptionCard)
            .selectable(selected = isSelected, onClick = { onItemClick(answer) }),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${getLetterOption(answerIndex)})",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = answer.text,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
            )
            if (isSelected) {
                Icon(
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
fun QuizQuestionContentPreview() {
    FarmaQuizTheme {
        Column {
            QuizQuestionContent(
                questionWithAnswers = CategoriesDummy.questions.first()
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

@Composable
@Preview(showSystemUi = true)
fun QuizAnswerListPreview() {
    FarmaQuizTheme {
        Column {
            QuizAnswerList(answers = CategoriesDummy.questions.first().answers,
                onItemClick = {},
                onSeeExplicationClick = {})
        }
    }
}

fun getLetterOption(index: Int): String {
    val options = listOf("A", "B", "C", "D")
    return options[index]
}