package ru.stanise.animebrowsing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.ui.MainScreen
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.stanise.animebrowsing.ui.model.WindowSizeModel


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeBrowsingTheme {
                val windowSize = calculateWindowSizeClass(this)
                App(windowSize.widthSizeClass)
            }
        }
    }
}

@Composable
fun App(
    windowWidth: WindowWidthSizeClass,
    windowSizeModel: WindowSizeModel = viewModel()
) {

    windowSizeModel.updateWidthState(windowWidth)
    val navigator = (LocalContext.current.applicationContext as AnimeApplication).container.navigator

    MainScreen(navigator = navigator, windowSizeModel = windowSizeModel)
}