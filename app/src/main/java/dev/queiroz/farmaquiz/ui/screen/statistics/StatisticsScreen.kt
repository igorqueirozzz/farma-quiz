package dev.queiroz.farmaquiz.ui.screen.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import dev.queiroz.farmaquiz.ui.Statistics
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme


@Composable
fun StatisticsScreen(
    scores: List<CategoryWithCategoryScore>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ElevatedCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.your_experience_by_category),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier.padding(start = 6.dp),
                    imageVector = Icons.Rounded.PieChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (scores.isNotEmpty()) {
                ExperienceByCategoryChart(
                    modifier = Modifier.padding(vertical = 16.dp), scores = scores
                )
            }
        }
    }
}

@Composable
fun ExperienceByCategoryChart(
    scores: List<CategoryWithCategoryScore>, modifier: Modifier = Modifier
) {

    val colors = listOf(
        Color(0xFF0802A3),
        Color(0xFFFF4B91),
        Color(0xFFFF7676),
        Color(0xFFFFCD4B),
        Color(0xFFA6FF96),
        Color(0xFF82A0D8),
        Color(0xFFBC7AF9),
        Color(0xFFFFA1F5),
        Color(0xFF008000),
        Color(0xFF808080),
        Color(0xFFFFC0CB),
        Color(0xFFFFFFE0),
        Color(0xFFADD8E6),
        Color(0xFF7FFFD4),
        Color(0xFF98FB98),
        Color(0xFFFFD700),
        Color(0xFF9400D3),
        Color(0xFF00BFFF),
        Color(0xFF008080),
        Color(0xFFFFFF99)
    )


    val totalOfScores = scores.sumOf { it.categoryScore.score }

    val pieChartData =
        PieChartData(plotType = PlotType.Pie, slices = scores.mapIndexed { index, value ->
            PieChartData.Slice(
                label = value.category.name,
                value = ((100 / totalOfScores) * value.categoryScore.score).toFloat(),
                color = colors[index]
            )
        })

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        labelVisible = false,
        showSliceLabels = false,
        animationDuration = 2000,
        backgroundColor = Color.Transparent
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween) {
        PieChart(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp), pieChartData, pieChartConfig
        )

        LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(items = scores) { index, item ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = item.category.name,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .weight(2f),
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors[index])
                            .size(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(100 / totalOfScores) * item.categoryScore.score}%",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            maxLines = 1
                            )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun ExperienceByCategoryChartPreview() {
    FarmaQuizTheme {
        ExperienceByCategoryChart(
            scores = emptyList()
        )
    }
}

