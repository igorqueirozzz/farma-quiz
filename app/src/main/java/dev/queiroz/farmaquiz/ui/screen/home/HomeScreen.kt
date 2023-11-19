package dev.queiroz.farmaquiz.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.components.CategoryCardList
import dev.queiroz.farmaquiz.ui.components.DailyCard
import dev.queiroz.farmaquiz.ui.components.ExperienceCard
import dev.queiroz.farmaquiz.ui.components.UserGreeting
import dev.queiroz.farmaquiz.ui.screen.viewmodel.HomeState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    state: HomeState,
    onCategorySelected: (Category) -> Unit,
    onMiscellaneousClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isContentVisible by rememberSaveable { mutableStateOf(state is HomeState.LoadedState) }
    when (state) {
        is HomeState.LoadedState -> {
            var totalOfExperiencePoints by remember { mutableIntStateOf(2) }
            val categoriesScoresFlow = state.categoriesScores
            LaunchedEffect(key1 = categoriesScoresFlow) {
                categoriesScoresFlow?.collectLatest { categoryScore ->
                    totalOfExperiencePoints = categoryScore.sumOf { it.categoryScore.score }
                }
            }
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .testTag(Home.name)
            ) {

                if (!isContentVisible) {
                    LaunchedEffect(Unit) {
                        isContentVisible = true
                    }
                }

                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = scaleIn()
                ) {
                    UserGreeting(
                        userName = state.userName
                    )
                }

                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = scaleIn(
                        animationSpec = tween(delayMillis = 100)
                    )
                ) {
                    ExperienceCard(
                        experiencePoints = totalOfExperiencePoints,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = scaleIn(
                        animationSpec = tween(delayMillis = 100)
                    )
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = stringResource(R.string.practice_more),
                            style = MaterialTheme.typography.titleLarge
                        )
                        DailyCard(
                            modifier = Modifier.clickable { onMiscellaneousClick() }
                        )
                    }
                }


                val categories = state.categories?.collectAsState(emptyList())

                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = slideInHorizontally(
                        initialOffsetX = { it * -1 },
                        animationSpec = tween(delayMillis = 250)
                    )
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                            text = stringResource(R.string.keep_studying),
                            style = MaterialTheme.typography.titleLarge
                        )

                        CategoryCardList(
                            modifier = Modifier.height(500.dp),
                            categories = categories?.value ?: emptyList(),
                            onItemClick = onCategorySelected
                        )
                    }
                }

            }
        }

        is HomeState.LoadingState -> CircularProgressIndicator()
    }
}