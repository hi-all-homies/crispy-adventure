package ru.stanise.animebrowsing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeBrowsingTheme {
                AnimeAppScreen()
            }
        }
    }
}