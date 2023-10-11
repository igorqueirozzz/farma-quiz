package dev.queiroz.farmaquiz.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.extensions.getDrawableIdentifier
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.components.CategoryCardList
import dev.queiroz.farmaquiz.ui.components.DailyCard
import dev.queiroz.farmaquiz.ui.components.ExperienceCard
import dev.queiroz.farmaquiz.ui.components.UserGreeting

@Composable
fun HomeScreen(
    state: HomeState,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is HomeState.LoadedState -> {
            val player by state.player.collectAsState(initial = Player(name = ""))
            val categoriesScores by state.categoriesScores.collectAsState(initial = emptyList())
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .testTag(Home.name)
            ) {

                UserGreeting(
                    userName = player.name,
                    painter = painterResource(
                        id = LocalContext.current.getDrawableIdentifier("training_brain")
                    )
                )

                ExperienceCard(
                    experiencePoints = categoriesScores.sumOf { categoryScore -> categoryScore.score },
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(R.string.practice_more),
                    style = MaterialTheme.typography.titleLarge
                )
                DailyCard()

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    text = stringResource(R.string.keep_studying),
                    style = MaterialTheme.typography.titleLarge
                )

                val categories by state.categories.collectAsState(initial = emptyList())
                CategoryCardList(
                    categories = categories,
                    onItemClick = onCategorySelected
                )

            }
        }

        is HomeState.LoadingState -> CircularProgressIndicator()
    }
}