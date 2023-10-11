package dev.queiroz.farmaquiz.ui.screen.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizDialog
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onUserPassWelcomeScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var startWelcomeMessageScreenAnimationOne by remember { mutableStateOf(false) }
    var startWelcomeMessageScreenAnimationTwo by remember { mutableStateOf(false) }
    var showRequestNameDialog by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }

    val screens = listOf<@Composable () -> Unit>(
        { WelcomeLogoApp() },
        {
            WelcomeScreenMessage(
                painter = painterResource(id = R.drawable.training_brain),
                text = stringResource(id = R.string.welcome_message_one),
                startAnimation = startWelcomeMessageScreenAnimationOne
            )
        },
        {
            WelcomeScreenMessage(
                painter = painterResource(id = R.drawable.learning_brain),
                text = stringResource(id = R.string.welcome_message_two),
                startAnimation = startWelcomeMessageScreenAnimationTwo
            ) {
                ElevatedButton(onClick = { showRequestNameDialog = true }) {
                    Text(text = "Pronto!", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    )


    val pageState = rememberPagerState {
        screens.size
    }

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.collect {
            when (it) {
                1 -> startWelcomeMessageScreenAnimationOne = true
                2 -> startWelcomeMessageScreenAnimationTwo = true
            }
        }
    }

    if (showRequestNameDialog) {
        var invalidField by remember { mutableStateOf(false) }
        QuizDialog(
            title = stringResource(R.string.how_about_introducing_yourself),
            text = "",
            onDismiss = {},
            onConfirm = {
                if (userName.isEmpty()) {
                    invalidField = true
                } else {
                    onUserPassWelcomeScreen(userName)
                    showRequestNameDialog = false
                }
            },
            centerContent = {
                TextField(
                    isError = invalidField,
                    value = userName,
                    onValueChange = { userName = it },
                    label = {
                        Text(text = stringResource(R.string.type_your_name))
                    }
                )
            },
            dialogProperties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
            ),
            showCancelButton = false
        )
    }

    Scaffold(modifier = modifier) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            state = pageState,

            ) { pageIndex ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            ) {
                screens[pageIndex].invoke()
            }
        }
    }

}

@Composable
fun WelcomeLogoApp(
    modifier: Modifier = Modifier
) {
    var imageSize by remember { mutableStateOf(0.dp) }
    val animatedSizeValue by animateDpAsState(
        targetValue = imageSize, label = "", animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
        )
    )


    LaunchedEffect(key1 = Unit) {
        delay(100)
        imageSize = 300.dp
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.farma_quiz_watermark),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier.size(animatedSizeValue),
                painter = painterResource(id = R.drawable.farma_quiz_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "${stringResource(R.string.welcome_to)} ${stringResource(id = R.string.app_name)}!",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.welcome_choice_message),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WelcomeScreenMessage(
    painter: Painter,
    text: String,
    startAnimation: Boolean,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit?)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .size(350.dp),
            visible = startAnimation,
            enter = slideInHorizontally() + fadeIn(),
            exit = ExitTransition.None
        ) {
            Box(
                contentAlignment = Alignment.Center, propagateMinConstraints = true
            ) {
                Image(
                    painter = painter, contentDescription = null
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            visible = startAnimation,
            enter = fadeIn(
                animationSpec = tween(
                    delayMillis = 500, durationMillis = 1000
                )
            ),
            exit = ExitTransition.None
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium
                )

                action?.invoke()
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun WelcomeScreenItemOnePreview() {
    FarmaQuizTheme {
        WelcomeLogoApp(modifier = Modifier.fillMaxSize())
    }
}

@Composable
@Preview(showSystemUi = true)
fun WelcomeScreenItemTwoPreview() {
    FarmaQuizTheme {
        WelcomeScreenMessage(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.learning_brain),
            text = stringResource(
                id = R.string.welcome_message_one
            ),
            startAnimation = true
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun WelcomeScreen() {
    FarmaQuizTheme {
        WelcomeScreen(modifier = Modifier.fillMaxSize(), onUserPassWelcomeScreen = {})
    }
}
