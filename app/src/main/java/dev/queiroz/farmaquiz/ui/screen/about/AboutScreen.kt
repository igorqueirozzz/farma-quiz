package dev.queiroz.farmaquiz.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            LocalContext.current.packageName,
    0
).versionName
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.farma_quiz_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(text = "v$versionName")

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