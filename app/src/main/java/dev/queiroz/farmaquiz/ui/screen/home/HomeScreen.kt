package dev.queiroz.farmaquiz.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    when (state) {
        is HomeState.LoadedState -> {
            var totalOfExperiencePoints by remember { mutableIntStateOf(2) }
            val categoriesScoresFlow = state.categoriesScores
            LaunchedEffect(key1 = categoriesScoresFlow){
                categoriesScoresFlow.collectLatest { categoryScore ->
                    totalOfExperiencePoints = categoryScore.sumOf { it.categoryScore.score }
                }
            }
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .testTag(Home.name)
            ) {

                UserGreeting(
                    userName = state.userName
                )

                ExperienceCard(
                    experiencePoints = totalOfExperiencePoints,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(R.string.practice_more),
                    style = MaterialTheme.typography.titleLarge
                )
                DailyCard(
                    modifier = Modifier.clickable { onMiscellaneousClick() }
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    text = stringResource(R.string.keep_studying),
                    style = MaterialTheme.typography.titleLarge
                )

                val categories by state.categories.collectAsState(initial = emptyList())

                CategoryCardList(
                    modifier = Modifier.height(500.dp),
                    categories = categories,
                    onItemClick = onCategorySelected
                )

            }
        }

        is HomeState.LoadingState -> CircularProgressIndicator()
    }
}