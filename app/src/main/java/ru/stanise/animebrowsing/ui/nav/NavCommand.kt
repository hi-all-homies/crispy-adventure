package ru.stanise.animebrowsing.ui.nav

sealed class NavCommand {
    data class To(val screen: AppScreen) : NavCommand()
    object Back : NavCommand()
}