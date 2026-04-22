package com.gitproject.redeglobo

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboBlueBright
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkGray
import com.gitproject.redeglobo.ui.theme.GloboDarkSurface
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GloboTvTheme(content: @Composable () -> Unit) {
    val tvColors = darkColorScheme(
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
        error              = GloboRed,
        onError            = GloboWhite,
        border             = GloboDarkGray,
        inverseSurface     = GloboWhite,
        inverseOnSurface   = GloboBlack
    )

    MaterialTheme(colorScheme = tvColors, content = content)
}
