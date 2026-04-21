package com.gitproject.redeglobo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GloboColorScheme = darkColorScheme(
    primary            = GloboBlue,
    onPrimary          = GloboWhite,
    primaryContainer   = GloboBlueBright,
    onPrimaryContainer = GloboWhite,
    secondary          = GloboRed,
    onSecondary        = GloboWhite,
    background         = GloboBlack,
    onBackground       = GloboWhite,
    surface            = GloboDarkSurface,
    onSurface          = GloboWhite,
    surfaceVariant     = GloboDarkCard,
    onSurfaceVariant   = GloboGray,
    surfaceContainer   = GloboDarkCard,
    error              = GloboRed,
    onError            = GloboWhite,
    outline            = GloboDarkGray,
    outlineVariant     = GloboDarkGray
)

@Composable
fun RedeGloboTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GloboColorScheme,
        typography  = GloboTypography,
        shapes      = GloboShapes,
        content     = content
    )
}
