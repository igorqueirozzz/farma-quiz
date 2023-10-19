package dev.queiroz.farmaquiz.model

import java.time.LocalDate


enum class ThemeMode{
    LIGHT,
    DARK,
    AUTO
}

data class UserPreferences(val userName: String, val themeMode: ThemeMode, val isFirstLaunch: Boolean, val lastDataUpdate: LocalDate?)
