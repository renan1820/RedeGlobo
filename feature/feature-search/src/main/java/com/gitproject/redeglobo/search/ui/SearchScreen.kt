package com.gitproject.redeglobo.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gitproject.redeglobo.ui.theme.RedeGloboTheme
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.search.presentation.SearchUiState
import com.gitproject.redeglobo.search.presentation.SearchViewModel
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkSurface
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite

@Composable
fun SearchScreen(
    onContentClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack)
            .statusBarsPadding()
    ) {
        TextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                viewModel.onQueryChanged(newQuery)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = { Text("Busque filmes, séries e mais", color = GloboGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = GloboBlue,
                    modifier = Modifier.size(22.dp)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor      = GloboDarkSurface,
                unfocusedContainerColor    = GloboDarkSurface,
                focusedTextColor           = GloboWhite,
                unfocusedTextColor         = GloboWhite,
                cursorColor                = GloboBlue,
                focusedIndicatorColor      = Color.Transparent,
                unfocusedIndicatorColor    = Color.Transparent,
                focusedLeadingIconColor    = GloboBlue,
                unfocusedLeadingIconColor  = GloboBlue
            )
        )

        when (val state = uiState) {
            is SearchUiState.Idle -> IdleState()

            is SearchUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = GloboBlue, strokeWidth = 2.dp) }

            is SearchUiState.Success -> SearchResultsGrid(
                results = state.results,
                onItemClick = onContentClick
            )

            is SearchUiState.Empty -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum resultado para \"${state.query}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GloboGray
                )
            }

            is SearchUiState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GloboRed
                )
            }
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

private val previewSearchResults = List(6) {
    Content(
        id = "$it", title = listOf("Rick e Morty", "Dragon Ball Z", "Naruto Shippuden",
            "Attack on Titan", "One Piece", "Demon Slayer")[it],
        thumbnailUrl = "", status = ContentStatus.ALIVE,
        genre = "Animação", originName = "Japão", episodeCount = 100 + it * 24
    )
}

@Preview(name = "Search — Idle", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SearchIdlePreview() {
    RedeGloboTheme { IdleState() }
}

@Preview(name = "Search — Resultados", showBackground = true, backgroundColor = 0xFF000000, heightDp = 700)
@Composable
private fun SearchResultsPreview() {
    RedeGloboTheme {
        SearchResultsGrid(results = previewSearchResults, onItemClick = {})
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun IdleState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GloboBlue,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Busque filmes, séries e mais",
                style = MaterialTheme.typography.bodyMedium,
                color = GloboGray
            )
        }
    }
}

@Composable
private fun SearchResultsGrid(results: List<Content>, onItemClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(results) { content ->
            SearchResultCard(content = content, onClick = { onItemClick(content.id) })
        }
    }
}

@Composable
private fun SearchResultCard(content: Content, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(GloboDarkCard)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = content.thumbnailUrl,
            contentDescription = content.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = content.title,
            style = MaterialTheme.typography.bodySmall,
            color = GloboWhite,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}
