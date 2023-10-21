package dev.queiroz.farmaquiz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.QuizDestination
import dev.queiroz.farmaquiz.ui.allTabScreens
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme

@Composable
fun QuizAppBar(
    allScreens: List<QuizDestination>,
    currentScreen: QuizDestination,
    onTabClick: (QuizDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier.selectableGroup(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            allScreens.forEach { screen ->
                QuizTabItem(
                    title = screen.name,
                    isSelected = currentScreen == screen,
                    icon = screen.icon,
                    onItemClick = {
                        onTabClick(screen)
                    })
            }

        }
    }
}

@Composable
fun QuizTabItem(
    title: String,
    isSelected: Boolean,
    icon: ImageVector,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val color = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurface
    val animDuration = if (isSelected) tabFadeInAnimationDuration else tabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = animDuration,
            delayMillis = tabAnimationDelay,
            easing = LinearEasing
        )
    }

    val tabColor by animateColorAsState(
        targetValue = if (isSelected) color else unselectedColor,
        label = "tabColorAnimation",
        animationSpec = animSpec
    )

    Row(
        modifier = modifier
            .height(56.dp)
            .padding(16.dp)
            .animateContentSize()
            .selectable(
                selected = isSelected,
                onClick = onItemClick,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            )
            .clearAndSetSemantics { contentDescription = title },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tabColor)

        if (isSelected) {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = tabColor
                )
            )
        }
    }
}


@Composable
@Preview
fun QuizAppBarPreview() {
    FarmaQuizTheme {
        QuizAppBar(allScreens = allTabScreens, currentScreen = Home, onTabClick = {})
    }
}

@Composable
@Preview(showBackground = true)
fun QuizTabItemPreview() {
    FarmaQuizTheme {
        QuizTabItem(
            title = "Preview",
            isSelected = false,
            icon = Icons.Filled.Home,
            onItemClick = {}
        )
    }
}

private val tabFadeInAnimationDuration = 200
private val tabFadeOutAnimationDuration = 100
private val tabAnimationDelay = 100