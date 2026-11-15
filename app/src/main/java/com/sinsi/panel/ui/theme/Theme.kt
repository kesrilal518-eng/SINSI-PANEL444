package com.sinsi.panel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SinsiDarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    secondary = ElectricGreen,
    onSecondary = Color.Black,
    tertiary = AccentOrange,
    background = BlackBackground,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    error = AccentRed
)

@Composable
fun SinsiPanelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SinsiDarkColorScheme,
        typography = Typography,
        content = content
    )
}
