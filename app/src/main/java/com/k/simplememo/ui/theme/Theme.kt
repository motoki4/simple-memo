package com.k.simplememo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    onPrimary = SurfaceDark,
    secondary = YellowAccent,
    onSecondary = SurfaceDark,
    background = SurfaceDark,
    onBackground = SurfaceLight,
    surface = SurfaceDark,
    onSurface = SurfaceLight,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextOnSurfaceVariantDark,
    tertiary = TealPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = SurfaceLight,
    secondary = YellowAccent,
    onSecondary = SurfaceLight,
    background = SurfaceLight,
    onBackground = SurfaceDark,
    surface = SurfaceLight,
    onSurface = SurfaceDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TealDark,
    tertiary = TealDark
)

@Composable
fun SimpleMemoAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !useDarkTheme
            insetsController.isAppearanceLightNavigationBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
