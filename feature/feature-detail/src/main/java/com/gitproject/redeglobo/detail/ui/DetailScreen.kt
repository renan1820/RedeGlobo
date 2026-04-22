package com.gitproject.redeglobo.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gitproject.redeglobo.ui.theme.RedeGloboTheme
import com.gitproject.redeglobo.detail.presentation.DetailUiState
import com.gitproject.redeglobo.detail.presentation.DetailViewModel
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.model.ContentLocation
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.model.Episode
import com.gitproject.redeglobo.domain.model.Origin
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkGray
import com.gitproject.redeglobo.ui.theme.GloboDarkSurface
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboGreen
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit = {},
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack)
    ) {
        when (val state = uiState) {
            is DetailUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = GloboBlue, strokeWidth = 2.dp) }

            is DetailUiState.Success -> DetailContent(
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
                    Spacer(Modifier.size(16.dp))
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GloboBlue),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text("Voltar") }
                }
            }
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 12.dp, start = 4.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = GloboWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DetailContent(
    detail: ContentDetail,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    onPlayClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { HeroSection(detail = detail) }
        item {
            Column(
                modifier = Modifier
                    .background(GloboBlack)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GloboBlue),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Assistir", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onWatchlistToggle,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GloboWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GloboDarkGray),
                    shape = RoundedCornerShape(4.dp)
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
        item { MetadataSection(detail = detail) }
        item {
            Text(
                text = "Episódios (${detail.episodes.size})",
                style = MaterialTheme.typography.titleMedium,
                color = GloboWhite,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
        items(detail.episodes) { episode ->
            EpisodeItem(episode = episode)
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

private val previewDetail = ContentDetail(
    id = "1", title = "Rick Sanchez", posterUrl = "",
    status = ContentStatus.ALIVE, genre = "Sci-Fi / Animação", type = "Personagem", gender = "Masculino",
    origin = Origin("1", "Terra C-137", "Planet", "Dimension C-137"),
    location = ContentLocation("1", "Citadela dos Ricks", "Space Station"),
    episodes = listOf(
        Episode("1", "Piloto", "S01E01", "02 Dez 2013"),
        Episode("2", "Lawnmower Dog", "S01E02", "09 Dez 2013"),
        Episode("3", "Anatomy Park", "S01E03", "16 Dez 2013")
    ),
    createdAt = "2017-11-04"
)

@Preview(name = "Detail — Conteúdo", showBackground = true, backgroundColor = 0xFF000000, heightDp = 900)
@Composable
private fun DetailContentPreview() {
    RedeGloboTheme {
        DetailContent(
            detail = previewDetail,
            isInWatchlist = false,
            onWatchlistToggle = {},
            onPlayClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Detail — Na Minha Lista", showBackground = true, backgroundColor = 0xFF000000, heightDp = 900)
@Composable
private fun DetailContentInWatchlistPreview() {
    RedeGloboTheme {
        DetailContent(
            detail = previewDetail.copy(status = ContentStatus.DEAD, title = "Morty Smith"),
            isInWatchlist = true,
            onWatchlistToggle = {},
            onPlayClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Detail — Episódio", showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun EpisodeItemPreview() {
    RedeGloboTheme {
        EpisodeItem(episode = Episode("1", "Piloto — Primeiro episódio da série", "S01E01", "02 Dez 2013"))
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(detail: ContentDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AsyncImage(
            model = detail.posterUrl,
            contentDescription = detail.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, GloboBlack),
                        startY = 100f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusDot(status = detail.status)
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = GloboWhite,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${detail.genre} • ${detail.type}",
                style = MaterialTheme.typography.bodySmall,
                color = GloboGray
            )
        }
    }
}

@Composable
private fun StatusDot(status: ContentStatus) {
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
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, RoundedCornerShape(50))
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
private fun MetadataSection(detail: ContentDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GloboDarkSurface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetadataRow(label = "Origem", value = detail.origin.name)
        MetadataRow(label = "Localização", value = detail.location.name)
        MetadataRow(label = "Dimensão", value = detail.origin.dimension.ifEmpty { "Desconhecida" })
    }
}

@Composable
private fun MetadataRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = GloboGray,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = GloboWhite
        )
    }
}

@Composable
private fun EpisodeItem(episode: Episode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GloboBlack)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = episode.code,
                style = MaterialTheme.typography.labelMedium,
                color = GloboBlue,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = episode.airDate,
                style = MaterialTheme.typography.labelSmall,
                color = GloboGray
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = episode.title,
            style = MaterialTheme.typography.bodyMedium,
            color = GloboWhite
        )
    }
    HorizontalDivider(color = GloboDarkCard, thickness = 0.5.dp)
}
