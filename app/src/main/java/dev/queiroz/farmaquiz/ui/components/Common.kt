package dev.queiroz.farmaquiz.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags.categoryCardItem
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.extensions.getDrawableIdentifier
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.ui.Settings
import dev.queiroz.farmaquiz.ui.screen.settings.getProfilePicture
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme

@Composable
fun UserGreeting(userName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.hi_user, userName.capitalize(Locale.current)),
                style = MaterialTheme.typography.displayMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = stringResource(R.string.great_to_see_you_again),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            bitmap = Settings.getProfilePicture(context = LocalContext.current).asImageBitmap(),
            contentDescription = "UserPhoto",
        )
    }
}


@Composable
fun CategoryCard(
    category: Category,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .clickable { onItemClick() }
        .testTag(categoryCardItem)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = LocalContext.current.getDrawableIdentifier(category.imageName)),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.tertiary)
                )

                if (category.remainingQuestions > 0) {
                    Text(
                        text = "${category.remainingQuestions} questões restantes",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCardList(
    categories: List<Category>,
    onItemClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
    horizontalPaddingValues: Dp = 0.dp,
    verticalPaddingValue: Dp = 0.dp
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = horizontalPaddingValues,
            vertical = verticalPaddingValue
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (categories.isEmpty()) {
            item {
                Text(text = stringResource(R.string.no_information_found))
            }
        } else {

            items(items = categories) { category ->
                CategoryCard(
                    modifier = Modifier.animateItemPlacement(),
                    category = category,
                    onItemClick = { onItemClick(category) })
            }
        }
    }

}

@Composable
fun ExperienceCard(experiencePoints: Int, modifier: Modifier = Modifier) {
    val animatedValue by animateIntAsState(
        targetValue = experiencePoints,
        label = "",
        animationSpec = tween(durationMillis = 500)
    )

    Card(
        modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(60.dp),
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = animatedValue.toString(),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = stringResource(R.string.experience_points))
            }
        }
    }
}

@Composable
fun DailyCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.daily_questions),
                    style = MaterialTheme.typography.displayMedium
                )
                Text(text = "20 questões sobre diversas categorias")
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            imageVector = Icons.Rounded.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (content != null) {
            content()
        }
    }
}


@Composable
@Preview(widthDp = 300)
fun UserGreetingPreview() {
    FarmaQuizTheme {
        Surface {
            UserGreeting(userName = "preview")
        }
    }
}


@Composable
@Preview
fun CategoryCardPreview() {
    FarmaQuizTheme {
        CategoryCard(category = CategoriesDummy.categories.first(), onItemClick = {})
    }
}

@Composable
@Preview(widthDp = 300)
fun ExperienceCardPreview() {
    FarmaQuizTheme {
        ExperienceCard(experiencePoints = 15000, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview(widthDp = 300)
fun DailyCardPreview() {
    FarmaQuizTheme {
        DailyCard()
    }
}

@Composable
@Preview
fun CategoryCardListPreview() {
    FarmaQuizTheme {
        CategoryCardList(categories = CategoriesDummy.categories, onItemClick = {})
    }
}

@Composable
@Preview(showSystemUi = true)
fun ErrorScreenPreview() {
    FarmaQuizTheme {
        ErrorScreen(message = "Conection error, Please try again") {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Try Again")
            }
        }
    }
}