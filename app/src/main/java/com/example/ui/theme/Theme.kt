package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = DarkTextPrimary,
    primaryContainer = GlassSurfaceDark,
    onPrimaryContainer = DarkTextPrimary,
    secondary = Purple500,
    onSecondary = DarkTextPrimary,
    tertiary = NeonCyan,
    background = GlassBgDark,
    onBackground = DarkTextPrimary,
    surface = GlassSurfaceDark,
    onSurface = DarkTextPrimary,
    surfaceVariant = GlassSurfaceDarkLow,
    onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Slate900,
    secondary = Purple500,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    tertiary = ElectricCyan500,
    background = GlassBgLight,
    onBackground = Slate900,
    surface = GlassSurfaceLight,
    onSurface = Slate900,
    surfaceVariant = GlassSurfaceLightLow,
    onSurfaceVariant = Slate700
)

@Composable
fun NextHireTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
