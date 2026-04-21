package com.gitproject.redeglobo.home.ui.tv

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentRail
import com.gitproject.redeglobo.home.presentation.HomeUiState
import com.gitproject.redeglobo.home.presentation.HomeViewModel
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenTv(
    onContentClick: (String) -> Unit,
    onPlayClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack)
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> TvLoadingState()
            is HomeUiState.Success -> TvHomeContent(
                rails = state.rails,
                onItemClick = onContentClick,
                onPlayClick = onPlayClick
            )
            is HomeUiState.Error -> TvErrorState(
                message = state.message,
                onRetry = viewModel::refresh
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvHomeContent(rails: List<ContentRail>, onItemClick: (String) -> Unit, onPlayClick: () -> Unit) {
    val heroContent = rails.firstOrNull()?.items?.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        if (heroContent != null) {
            item { TvHeroBanner(content = heroContent, onPlayClick = onPlayClick) }
        }
        items(rails) { rail ->
            TvContentRailRow(rail = rail, onItemClick = onItemClick)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvHeroBanner(content: Content, onPlayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
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
                        colors = listOf(Color.Transparent, GloboBlack.copy(alpha = 0.6f), GloboBlack),
                        startY = 160f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 56.dp, end = 56.dp, bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(GloboRed, RoundedCornerShape(2.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(text = "EM ALTA", style = MaterialTheme.typography.labelSmall, color = GloboWhite)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineLarge,
                color = GloboWhite,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${content.genre} • ${content.episodeCount} eps",
                style = MaterialTheme.typography.bodyMedium,
                color = GloboGray
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.colors(containerColor = GloboBlue),
                    shape = androidx.tv.material3.ButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Assistir")
                }
                OutlinedButton(
                    onClick = {},
                    shape = androidx.tv.material3.OutlinedButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Minha Lista")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvContentRailRow(rail: ContentRail, onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 28.dp)) {
        Text(
            text = rail.title,
            style = MaterialTheme.typography.titleLarge,
            color = GloboWhite,
            modifier = Modifier.padding(horizontal = 56.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(rail.items) { content ->
                TvContentCard(content = content, onClick = { onItemClick(content.id) })
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvContentCard(content: Content, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(180.dp)
    ) {
        Column(modifier = Modifier.background(GloboDarkCard)) {
            AsyncImage(
                model = content.thumbnailUrl,
                contentDescription = content.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = content.title,
                style = MaterialTheme.typography.bodyMedium,
                color = GloboWhite,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Carregando...", style = MaterialTheme.typography.titleLarge, color = GloboWhite)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge, color = GloboRed)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.colors(containerColor = GloboBlue),
                shape = androidx.tv.material3.ButtonDefaults.shape(shape = RoundedCornerShape(4.dp))
            ) { Text("Tentar novamente") }
        }
    }
}
