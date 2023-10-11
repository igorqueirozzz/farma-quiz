package dev.queiroz.farmaquiz.model


enum class ThemeMode{
    LIGHT,
    DARK,
    AUTO
}

data class UserPreferences(val userName: String, val themeMode: ThemeMode, val isFirstLaunch: Boolean)
