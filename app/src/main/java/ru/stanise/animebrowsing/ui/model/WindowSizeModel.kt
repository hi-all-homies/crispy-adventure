package ru.stanise.animebrowsing.ui.model

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WindowSizeModel : ViewModel() {
    private val _windowWidth = MutableStateFlow(WindowWidthSizeClass.Compact)
    val windowWidthState: StateFlow<WindowWidthSizeClass> = _windowWidth.asStateFlow()


    fun updateWidthState(newSizeClass: WindowWidthSizeClass) {
        _windowWidth.update { current ->
            if (current != newSizeClass) newSizeClass else current
        }
    }
}