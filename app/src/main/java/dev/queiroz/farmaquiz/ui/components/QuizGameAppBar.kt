package dev.queiroz.farmaquiz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.ui.theme.DoseDeConhecimentoTheme
import dev.queiroz.farmaquiz.R

@Composable
fun QuizGameAppBar(
    categoryName: String,
    currentQuestionIndex: Int,
    totalOfQuestions: Int,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
    ) {
        QuizGameBarAction(
            categoryName = categoryName,
            onBackClick = onBackClick,
            onSkipClick = onSkipClick
        )

        QuizGameBarScore(
            currentQuestionIndex = currentQuestionIndex,
            totalOfQuestions = totalOfQuestions
        )

    }
}

@Composable
fun QuizGameBarAction(
    categoryName: String,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Rounded.ChevronLeft, contentDescription = null)
        }
        Text(text = categoryName)
        TextButton(onClick = onSkipClick) {
            Text(text = stringResource(R.string.skip))
        }
    }
}

@Composable
fun QuizGameBarScore(
    currentQuestionIndex: Int,
    totalOfQuestions: Int,
    modifier: Modifier = Modifier
) {
    val progressValue by animateFloatAsState(
        targetValue = currentQuestionIndex.toFloat() / totalOfQuestions.toFloat(),
        label = "progressIncreaseAnimation",
        animationSpec = tween(
            delayMillis = 100,
            durationMillis = 500,
        )
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(25.dp)
                .clip(RoundedCornerShape(10.dp)),
            progress = progressValue,
        )

        Text(
            text = "$currentQuestionIndex / $totalOfQuestions",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (progressValue < 0.5f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}


@Composable
@Preview(showSystemUi = true)
fun QuizGameBarPreview() {
    DoseDeConhecimentoTheme {
        var currentQuestionIndex by remember {
            mutableIntStateOf(1)
        }
        QuizGameAppBar(
            categoryName = "Quimica",
            currentQuestionIndex = currentQuestionIndex,
            totalOfQuestions = 20,
            onBackClick = { /*TODO*/ },
            onSkipClick = { currentQuestionIndex++ })
    }
}