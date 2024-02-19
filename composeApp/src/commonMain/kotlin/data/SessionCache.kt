package data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

// Singleton...
object SessionCache {
    var configDevice: CrossConfigDevice? = null

    @Composable
    fun isDarkMode(): Boolean {
        return configDevice?.isDarkModeEnabled() ?: isSystemInDarkTheme()
    }
}
