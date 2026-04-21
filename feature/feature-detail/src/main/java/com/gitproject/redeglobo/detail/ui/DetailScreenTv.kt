package com.gitproject.redeglobo.detail.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedButtonDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.gitproject.redeglobo.detail.presentation.DetailUiState
import com.gitproject.redeglobo.detail.presentation.DetailViewModel
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.model.Episode
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboBlueBright
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkGray
import com.gitproject.redeglobo.ui.theme.GloboDarkSurface
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboGreen
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DetailScreenTv(
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit = {},
    viewModel: DetailViewModel = hiltViewModel()
) {
    BackHandler(onBack = onBackClick)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DetailUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Carregando...",
                    style = MaterialTheme.typography.titleLarge,
                    color = GloboWhite
                )
            }

            is DetailUiState.Success -> TvDetailContent(
                detail = state.detail,
                isInWatchlist = state.isInWatchlist,
                onWatchlistToggle = viewModel::toggleWatchlist,
                onPlayClick = onPlayClick,
                onBackClick = onBackClick
            )

            is DetailUiState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = GloboRed)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.colors(containerColor = GloboBlue),
                        shape = ButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
                    ) { Text("Voltar") }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvDetailContent(
    detail: ContentDetail,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    onPlayClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        item {
            TvHeroSection(
                detail = detail,
                isInWatchlist = isInWatchlist,
                onWatchlistToggle = onWatchlistToggle,
                onPlayClick = onPlayClick,
                onBackClick = onBackClick
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GloboDarkSurface)
                    .padding(horizontal = 56.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TvMetadataRow(label = "Origem", value = detail.origin.name)
                TvMetadataRow(label = "Localização", value = detail.location.name)
                TvMetadataRow(label = "Dimensão", value = detail.origin.dimension.ifEmpty { "Desconhecida" })
            }
        }
        if (detail.episodes.isNotEmpty()) {
            item {
                Text(
                    text = "Episódios (${detail.episodes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = GloboWhite,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 56.dp, vertical = 16.dp)
                )
            }
            items(detail.episodes) { episode ->
                TvEpisodeItem(episode = episode)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvHeroSection(
    detail: ContentDetail,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    onPlayClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
    ) {
        AsyncImage(
            model = detail.posterUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Gradient: bottom-to-top fade to black
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GloboBlack.copy(alpha = 0.2f), GloboBlack),
                        startY = 180f
                    )
                )
        )
        // Gradient: left side darker for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(GloboBlack.copy(alpha = 0.75f), Color.Transparent),
                        endX = 900f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 56.dp, end = 56.dp, top = 28.dp, bottom = 32.dp)
        ) {
            // Back button — in focus chain, not floating overlay
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.colors(
                    containerColor = GloboBlack.copy(alpha = 0.55f),
                    contentColor = GloboWhite,
                    focusedContainerColor = GloboBlue,
                    focusedContentColor = GloboWhite
                ),
                shape = ButtonDefaults.shape(shape = RoundedCornerShape(4.dp)),
                modifier = Modifier.height(38.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Voltar", style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Title + status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TvStatusDot(status = detail.status)
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = GloboWhite,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${detail.genre} • ${detail.type}",
                style = MaterialTheme.typography.bodyMedium,
                color = GloboGray
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.colors(
                        containerColor = GloboBlue,
                        contentColor = GloboWhite,
                        focusedContainerColor = GloboBlueBright,
                        focusedContentColor = GloboWhite
                    ),
                    shape = ButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Assistir", style = MaterialTheme.typography.labelLarge)
                }
                OutlinedButton(
                    onClick = onWatchlistToggle,
                    shape = OutlinedButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = if (isInWatchlist) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isInWatchlist) "Na Minha Lista" else "Minha Lista")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvStatusDot(status: ContentStatus) {
    val color = when (status) {
        ContentStatus.ALIVE   -> GloboGreen
        ContentStatus.DEAD    -> GloboRed
        ContentStatus.UNKNOWN -> GloboDarkGray
    }
    val label = when (status) {
        ContentStatus.ALIVE   -> "Vivo"
        ContentStatus.DEAD    -> "Morto"
        ContentStatus.UNKNOWN -> "Desconhecido"
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, RoundedCornerShape(50))
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvMetadataRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = GloboGray,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = GloboWhite
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvEpisodeItem(episode: Episode) {
    Surface(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 56.dp, vertical = 3.dp),
        shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(4.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = GloboDarkCard,
            focusedContainerColor = GloboBlue.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = episode.code,
                    style = MaterialTheme.typography.labelMedium,
                    color = GloboBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GloboWhite
                )
            }
            Text(
                text = episode.airDate,
                style = MaterialTheme.typography.labelSmall,
                color = GloboGray
            )
        }
    }
}
