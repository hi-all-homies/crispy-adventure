package ru.stanise.animebrowsing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.ui.MainScreen
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeBrowsingTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navigator = (LocalContext.current.applicationContext as AnimeApplication).container.navigator
    MainScreen(navigator = navigator)
}