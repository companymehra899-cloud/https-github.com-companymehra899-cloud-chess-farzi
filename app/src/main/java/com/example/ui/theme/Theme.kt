package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ChessDarkColorScheme = darkColorScheme(
    primary = ChessGold,
    secondary = ChessCyan,
    tertiary = ChessAmber,
    background = ChessDarkBg,
    surface = ChessSurface,
    surfaceVariant = ChessSurfaceVariant,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = ChessTextPrimary,
    onSurface = ChessTextPrimary,
    onSurfaceVariant = ChessTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark aesthetic for premium chess experience
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ChessDarkColorScheme,
        typography = Typography,
        content = content
    )
}
