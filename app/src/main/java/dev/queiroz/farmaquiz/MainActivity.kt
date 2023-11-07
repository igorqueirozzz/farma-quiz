package dev.queiroz.farmaquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.queiroz.farmaquiz.ui.QuizApp
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var PACKAGE_NAME = "dev.queiroz.farmaquiz"
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PACKAGE_NAME = packageName
        setContent {
            FarmaQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    QuizApp()
                }
            }
        }
    }
}
