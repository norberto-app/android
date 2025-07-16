package com.napp.mvvmdemo.ui.main


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napp.mvvmdemo.ui.common.ErrorContent
import com.napp.mvvmdemo.ui.common.LoadingContent
import com.napp.mvvmdemo.demoapp.R

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onItemSelected: (id: Int) -> Unit,
) {
    // Collect data from the ViewModel and react to it
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(modifier, uiState, onItemSelected)
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onItemSelected: (id: Int) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MainScreenTopAppBar(modifier)
        }
    ) { innerPadding ->
        when (uiState) {
            is MainUiState.Loading -> LoadingContent(
                modifier = modifier.padding(innerPadding)
            )

            is MainUiState.Success -> MainListContent(
                uiItems = uiState.items,
                onItemSelected = onItemSelected
            )

            is MainUiState.Error -> ErrorContent(
                modifier = modifier.padding(innerPadding),
                message = uiState.message
            )
        }
    }
}

@Composable
private fun MainListContent(
    uiItems: List<MainListItemUiState>,
    onItemSelected: (id: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val columnSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

    // Display the appropriate content based on the UI state
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnSize),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(uiItems) { item ->
            CardItemComposable(item = item, onItemSelected = onItemSelected)
        }
    }
}

@Composable
private fun CardItemComposable(
    item: MainListItemUiState,
    onItemSelected: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(0f)
            .defaultMinSize(minHeight = 100.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            onItemSelected(item.id)
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title ?: "-",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .height(2.dp)
            )
            Text(
                text = item.description ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.main_list_top_bar_title),
                    modifier = modifier,
                    fontSize = 20.sp
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier.fillMaxWidth()
    )
}
