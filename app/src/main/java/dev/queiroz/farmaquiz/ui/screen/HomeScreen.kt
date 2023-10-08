package dev.queiroz.farmaquiz.ui.screen

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.data.CategoriesDummy
import dev.queiroz.farmaquiz.ui.components.CategoryCardList
import dev.queiroz.farmaquiz.ui.components.DailyCard
import dev.queiroz.farmaquiz.ui.components.ExperienceCard
import dev.queiroz.farmaquiz.ui.components.UserGreeting
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.ui.Home

@Composable
fun HomeScreen(
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(horizontal = 16.dp)
        .testTag(Home.name)
    ) {
        UserGreeting(
            userName = "Glezia",
            painter = painterResource(
                id = R.drawable.pic_demo
            )
        )

        ExperienceCard(experiencePoints = 0, modifier = Modifier.padding(vertical = 16.dp))

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

        CategoryCardList(categories = CategoriesDummy.categories, onItemClick = onCategorySelected)

    }
}