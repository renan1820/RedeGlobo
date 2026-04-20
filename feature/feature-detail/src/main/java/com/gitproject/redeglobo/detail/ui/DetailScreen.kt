package com.gitproject.redeglobo.detail.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gitproject.redeglobo.detail.presentation.DetailUiState
import com.gitproject.redeglobo.detail.presentation.DetailViewModel
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.model.Episode

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        when (val state = uiState) {
            is DetailUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is DetailUiState.Success -> DetailContent(
                detail = state.detail,
                isInWatchlist = state.isInWatchlist,
                onWatchlistToggle = viewModel::toggleWatchlist,
                modifier = Modifier.padding(paddingValues)
            )

            is DetailUiState.Error -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.size(16.dp))
                    Button(onClick = onBackClick) { Text("Voltar") }
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    detail: ContentDetail,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AsyncImage(
                model = detail.posterUrl,
                contentDescription = detail.title,
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = detail.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = detail.genre, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "•")
                    Text(text = detail.status.name, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Origem: ${detail.origin.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onWatchlistToggle, modifier = Modifier.fillMaxWidth()) {
                    Text(if (isInWatchlist) "Remover da lista" else "Assistir depois")
                }
                Spacer(Modifier.height(24.dp))
                Text(text = "Episódios (${detail.episodes.size})", style = MaterialTheme.typography.titleMedium)
            }
        }
        items(detail.episodes) { episode ->
            EpisodeItem(episode = episode)
        }
    }
}

@Composable
private fun EpisodeItem(episode: Episode) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = episode.code, style = MaterialTheme.typography.labelMedium)
        Text(text = episode.title, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = episode.airDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
