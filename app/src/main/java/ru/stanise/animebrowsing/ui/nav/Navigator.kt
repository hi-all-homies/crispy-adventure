package ru.stanise.animebrowsing.ui.nav

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {
    private val _commands = MutableSharedFlow<NavCommand>(extraBufferCapacity = 1)
    val commands: SharedFlow<NavCommand> = _commands.asSharedFlow()

    fun navigateTo(screen: AppScreen) {
        _commands.tryEmit(NavCommand.To(screen))
    }

    fun back() {
        _commands.tryEmit(NavCommand.Back)
    }
}