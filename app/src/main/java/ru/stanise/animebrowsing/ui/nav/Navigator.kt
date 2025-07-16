package ru.stanise.animebrowsing.ui.nav

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {
    private val _commands = MutableSharedFlow<NavCommand>(extraBufferCapacity = 1)
    val commands: SharedFlow<NavCommand> = _commands.asSharedFlow()

    suspend fun navigateTo(screen: AppScreen) {
        _commands.emit(NavCommand.To(screen))
    }

    suspend fun back() {
        _commands.emit(NavCommand.Back)
    }
}