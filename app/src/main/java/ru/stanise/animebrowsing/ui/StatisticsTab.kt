package ru.stanise.animebrowsing.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import ru.stanise.animebrowsing.config.Config
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.countGenres
import kotlin.math.roundToInt


@Composable
fun GenresTab(
    faves: List<Anime>,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
){
    var pieData by remember { mutableStateOf(PieChartData(emptyList(), PlotType.Donut)) }
    var clickedGenre by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val slices = faves.countGenres()
            .entries
            .mapIndexed { index, entry ->
                PieChartData.Slice(label = entry.key, value = entry.value.toFloat(), color = Config.CHART_COLORS[index])
            }
        pieData = PieChartData(slices, PlotType.Donut)
    }

    val labelColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        if (pieData.slices.isNotEmpty()){
            when(windowWidthSizeClass){
                WindowWidthSizeClass.Compact -> {

                    val chartConfig = PieChartConfig(
                        labelVisible = true,
                        labelFontSize = 42.sp,
                        strokeWidth = 150f,
                        activeSliceAlpha = .9f,
                        isAnimationEnable = true,
                        animationDuration = 1000,
                        chartPadding = 20,
                        labelColor = labelColor,
                        backgroundColor = backgroundColor
                    )

                    item {
                        TextCard(modifier) {
                            Text(
                                text = "Genres you like the most",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        DonutPieChart(
                            modifier = modifier.fillMaxWidth(),
                            pieData,
                            chartConfig
                        ){ slice -> clickedGenre = "${slice.label}, count: ${slice.value.roundToInt()}" }

                        TextCard(modifier) {
                            SlidingText(clickedGenre)
                        }
                    }
                }
                else -> {
                    val chartConfig = PieChartConfig(
                        labelVisible = true,
                        labelFontSize = 32.sp,
                        strokeWidth = 90f,
                        activeSliceAlpha = .9f,
                        isAnimationEnable = true,
                        animationDuration = 1000,
                        chartPadding = 20,
                        labelColor = labelColor,
                        backgroundColor = backgroundColor
                    )
                    item {
                        Row(
                            modifier = modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DonutPieChart(
                                modifier = modifier.height(250.dp),
                                pieData,
                                chartConfig
                            ){ slice -> clickedGenre = "${slice.label}, count: ${slice.value.roundToInt()}" }

                            TextCard(modifier) {
                                SlidingText(clickedGenre)
                            }
                        }
                    }
                }
            }
        }
        else {
            item {
                TextCard(modifier) {
                    Text(
                        text = "You haven't added any anime to faves yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Composable
fun TextCard(modifier: Modifier = Modifier, content: @Composable () -> Unit){
    Card {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth().padding(8.dp)
        ) {
            content()
        }
    }
}


@Composable
fun SlidingText(text: String) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn()).togetherWith(
                    slideOutVertically { height -> -height } + fadeOut())
        },
        contentAlignment = Alignment.Center
    ) { animatedText ->
        Text(
            text = animatedText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPieChart(){
    GenresTab(listOf(), WindowWidthSizeClass.Compact)
}