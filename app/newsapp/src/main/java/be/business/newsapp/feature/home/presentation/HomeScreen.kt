package be.business.newsapp.feature.home.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.presentation.collectEvents
import be.business.newsapp.feature.home.components.TopNewsSection
import be.business.newsapp.ui.shared.ErrorView
import be.business.newsapp.ui.shared.LoadingSkeletonList
import coil.ImageLoader

@Composable
fun HomeScreen(
    onArticleClick: (String) -> Unit,
    onLoginRequired: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectEvents {
        when (it) {
            is HomeEvent.ShowToast -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            HomeEvent.NavigateToLogin -> onLoginRequired()
        }
    }

    HomeContent(
        uiState = uiState,
        imageLoader = viewModel.imageLoader,
        chipSelected = viewModel.selectedCategory,
        selectedCountry = viewModel.selectedCountry,
        onRetry = { viewModel.action(HomeAction.Retry) },
        onFavClick = { viewModel.action(HomeAction.AddToFavourites(it)) },
        onChipClick = { viewModel.selectCategory(it.ifBlank { null }) },
        onCountrySelected = { viewModel.selectCountry(it) },
        onArticleClick = onArticleClick
    )
}

@Composable
fun HomeContent(
    uiState: HomeState,
    imageLoader: ImageLoader,
    chipSelected: String? = null,
    selectedCountry: String? = null,
    onRetry: () -> Unit,
    onFavClick: (Article) -> Unit,
    onChipClick: (String) -> Unit,
    onCountrySelected: (String) -> Unit,
    onArticleClick: (String) -> Unit
) {
    when {
        uiState.error != null && uiState.articles.isEmpty() -> {
            ErrorView(uiState.error) { onRetry() }
        }

        uiState.loading && uiState.articles.isEmpty() -> {
            LoadingSkeletonList()
        }

        else -> {
            TopNewsSection(
                articles = uiState.articles,
                imageLoader = imageLoader,
                onFavClick = onFavClick,
                chipSelected = chipSelected,
                selectedCategory = onChipClick,
                isLoading = uiState.loading,
                showChips = true,
                countrySelected = selectedCountry,
                onCountrySelected = onCountrySelected,
                showFilterView = true,
                onArticleClick = onArticleClick
            )
        }
    }
}
