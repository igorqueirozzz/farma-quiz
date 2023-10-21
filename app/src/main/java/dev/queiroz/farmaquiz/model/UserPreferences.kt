package dev.queiroz.farmaquiz.model

import java.time.LocalDate


enum class ThemeMode{
    LIGHT,
    DARK,
    AUTO,
    DYNAMIC
}

data class UserPreferences(var userName: String, var themeMode: ThemeMode, val isFirstLaunch: Boolean, val lastDataUpdate: LocalDate?)
