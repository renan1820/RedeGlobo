package com.gitproject.redeglobo.home.ui.mobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentRail
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.home.presentation.HomeUiState
import com.gitproject.redeglobo.home.presentation.HomeViewModel
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkGray
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboGreen
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@Composable
fun HomeScreenMobile(
    onContentClick: (String) -> Unit,
    onPlayClick: () -> Unit = {},
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack)
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> LoadingState()
            is HomeUiState.Success -> HomeContent(
                rails = state.rails,
                onItemClick = onContentClick,
                onPlayClick = onPlayClick
            )
            is HomeUiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::refresh
            )
        }
    }
}

@Composable
private fun HomeContent(rails: List<ContentRail>, onItemClick: (String) -> Unit, onPlayClick: () -> Unit) {
    val heroContent = rails.firstOrNull()?.items?.firstOrNull()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (heroContent != null) {
            item { HeroBanner(content = heroContent, onPlayClick = onPlayClick) }
        }
        items(rails) { rail ->
            ContentRailRow(rail = rail, onItemClick = onItemClick)
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun HeroBanner(content: Content, onPlayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        AsyncImage(
            model = content.thumbnailUrl,
            contentDescription = content.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, GloboBlack.copy(alpha = 0.5f), GloboBlack),
                        startY = 120f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(GloboRed, RoundedCornerShape(2.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = "EM ALTA", style = MaterialTheme.typography.labelSmall, color = GloboWhite)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineMedium,
                color = GloboWhite,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${content.genre} • ${content.episodeCount} eps",
                style = MaterialTheme.typography.bodySmall,
                color = GloboGray
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GloboBlue),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Assistir", style = MaterialTheme.typography.labelLarge)
                }
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GloboWhite),
                    border = BorderStroke(1.dp, GloboWhite),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Minha Lista", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun ContentRailRow(rail: ContentRail, onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text(
            text = rail.title,
            style = MaterialTheme.typography.titleLarge,
            color = GloboWhite,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rail.items) { content ->
                ContentCard(content = content, onClick = { onItemClick(content.id) })
            }
        }
    }
}

@Composable
private fun ContentCard(content: Content, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(106.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(GloboDarkCard)
        ) {
            AsyncImage(
                model = content.thumbnailUrl,
                contentDescription = content.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            StatusBadge(status = content.status, modifier = Modifier.align(Alignment.TopStart).padding(4.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content.title,
            style = MaterialTheme.typography.bodySmall,
            color = GloboWhite,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusBadge(status: ContentStatus, modifier: Modifier = Modifier) {
    val (color, label) = when (status) {
        ContentStatus.ALIVE   -> GloboGreen   to "VIVO"
        ContentStatus.DEAD    -> GloboRed     to "MORTO"
        ContentStatus.UNKNOWN -> GloboDarkGray to "?"
    }
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.85f), RoundedCornerShape(2.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = GloboWhite)
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = GloboBlue, strokeWidth = 2.dp)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GloboRed)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = GloboBlue),
                shape = RoundedCornerShape(4.dp)
            ) { Text("Tentar novamente") }
        }
    }
}
