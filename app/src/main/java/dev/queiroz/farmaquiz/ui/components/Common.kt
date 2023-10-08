package dev.queiroz.farmaquiz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.data.CategoriesDummy
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.constants.TestTags.categoryCardItem

@Composable
fun UserGreeting(userName: String, modifier: Modifier = Modifier, painter: Painter? = null) {
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
            painter = painter ?: painterResource(id = R.drawable.ic_launcher_background),
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
    Card(modifier = modifier.clickable { onItemClick() }.testTag(categoryCardItem)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = category.imageId),
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

                Text(text = "20 questões restantes", style = MaterialTheme.typography.bodyLarge)
            }

            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

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
        items(items = categories) { category ->
            CategoryCard(category = category, onItemClick = { onItemClick(category) })
        }
    }
}

@Composable
fun ExperienceCard(experiencePoints: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                    text = experiencePoints.toString(),
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
                Text(text = "Perguntas Diárias", style = MaterialTheme.typography.displayMedium)
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
@Preview(widthDp = 300)
fun UserGreetingPreview() {
    FarmaQuizTheme {
        Surface {
            UserGreeting(userName = "preview", painter = painterResource(id = R.drawable.pic_demo))
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