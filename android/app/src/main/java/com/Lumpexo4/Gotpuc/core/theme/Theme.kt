package com.Lumpexo4.Gotpuc.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MeadowColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = SurfaceCream,
    primaryContainer = BrandGold,
    onPrimaryContainer = TextPrimary,
    secondary = MeadowSage,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = ClayWarn,
    onTertiary = SurfaceCream,
    tertiaryContainer = ParchmentDeep,
    onTertiaryContainer = TextPrimary,
    background = Parchment,
    onBackground = TextPrimary,
    surface = SurfaceCream,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = Hairline,
    outlineVariant = ParchmentDeep,
    error = ClayWarn,
    onError = SurfaceCream
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MeadowColorScheme,
        typography = MeadowTypography,
        content = content
    )
}
