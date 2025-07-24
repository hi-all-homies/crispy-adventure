package ru.stanise.animebrowsing.ui.nav

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

val enterTransition = slideInHorizontally(animationSpec = tween(700, easing = LinearOutSlowInEasing)) { it } + fadeIn(animationSpec = tween(700))

val exitTransition = slideOutHorizontally(animationSpec = tween(700, easing = LinearOutSlowInEasing)) { -it } + fadeOut(animationSpec = tween(700))

val popEnterTransition = slideInHorizontally(animationSpec = tween(700, easing = LinearOutSlowInEasing)) { -it } + fadeIn(animationSpec = tween(700))

val popExitTransition = slideOutHorizontally(animationSpec = tween(700, easing = LinearOutSlowInEasing)) { it } + fadeOut(animationSpec = tween(700))