package dev.queiroz.farmaquiz.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController>{ error("Navcontroller not found!") }