package be.business.newsapp.feature.favorites.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.presentation.collectEvents
import be.business.newsapp.feature.favorites.presentation.FavoritesAction
import be.business.newsapp.feature.favorites.presentation.FavoritesEvent
import be.business.newsapp.feature.favorites.presentation.FavoritesState
import be.business.newsapp.feature.favorites.presentation.FavoritesViewModel
import be.business.newsapp.feature.home.components.TopNewsSection
import be.business.newsapp.ui.shared.EmptyView
import be.business.newsapp.ui.shared.ErrorView
import be.business.newsapp.ui.shared.LoadingSkeletonList
import coil.ImageLoader

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectEvents {
        if (it is FavoritesEvent.ShowMessage) {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    FavoritesContent(
        state = state,
        imageLoader = viewModel.imageLoader,
        onRetry = { viewModel.action(FavoritesAction.LoadFavorites) },
        onToggleFavorite = { viewModel.action(FavoritesAction.ToggleFavorite(it)) }
    )
}

@Composable
fun FavoritesContent(
    state: FavoritesState,
    imageLoader: ImageLoader,
    onRetry: () -> Unit,
    onToggleFavorite: (Article) -> Unit
) {
    when {
        state.isLoading -> LoadingSkeletonList()
        state.error != null -> ErrorView(message = state.error, onRetry = onRetry)
        state.articles.isEmpty() -> EmptyView(message = "No favorites yet. Save articles from Home.")
        else -> TopNewsSection(
            articles = state.articles,
            selectedCategory = {},
            imageLoader = imageLoader,
            onFavClick = onToggleFavorite,
            showChips = false,
            showFilterView = false
        )
    }
}

@Composable
fun FavoritesLoginPrompt(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login Required",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Please login to view and manage your favorites.",
                    textAlign = TextAlign.Center
                )
                Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Go to Login")
                }
            }
        }
    }
}
