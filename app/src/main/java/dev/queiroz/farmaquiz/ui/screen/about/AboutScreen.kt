package dev.queiroz.farmaquiz.ui.screen.about

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme


@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    versionName: String = LocalContext.current.packageManager.getPackageInfo(
        LocalContext.current.packageName, 0
    ).versionName
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(visible = isVisible, enter = fadeIn()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.farma_quiz_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    text = "v$versionName"
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(delayMillis = 100)
            )
        ) {
            Text(
                text = "Contribuintes 👨🏻‍💻👩🏻‍️",
                style = MaterialTheme.typography.titleLarge
            )
        }


        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it * -1 },
                animationSpec = tween(delayMillis = 200)
            ) + fadeIn()
        ) {
            TeamContributorCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                name = "Igor Queiroz",
                job = "Desenvolvimento",
                linkedinLink = "https://www.linkedin.com/in/igor-queiroz-iq21/",
                picturePainter = painterResource(id = R.drawable.pic_igor)
            )
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it * -1 },
                animationSpec = tween(delayMillis = 300)
            ) + fadeIn()
        ) {
            TeamContributorCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                name = "Dra. Glezia Araujo",
                job = "Produção de conteúdo",
                linkedinLink = "https://www.linkedin.com/in/glezia-araujo/",
                picturePainter = painterResource(id = R.drawable.pic_glezia)
            )
        }


    }
}


@Composable
fun TeamContributorCard(
    name: String,
    job: String,
    linkedinLink: String,
    picturePainter: Painter,
    modifier: Modifier = Modifier
) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedinLink))
    val context = LocalContext.current
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    painter = picturePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(text = name, style = MaterialTheme.typography.titleLarge)
                    Text(text = job)
                }
            }

            IconButton(onClick = {
                context.startActivity(browserIntent)
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_linkedin),
                    contentDescription = null
                )
            }

        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AboutScreenPreview() {
    FarmaQuizTheme {
        AboutScreen(
            versionName = "1.0.0"
        )
    }
}