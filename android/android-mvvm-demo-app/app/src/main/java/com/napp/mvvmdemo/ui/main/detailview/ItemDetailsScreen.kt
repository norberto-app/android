package com.napp.mvvmdemo.ui.main.detailview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napp.mvvmdemo.ui.common.ErrorContent
import com.napp.mvvmdemo.ui.common.LoadingContent
import com.napp.mvvmdemo.ui.navigation.SelectedItem

@Composable
fun ItemDetailsScreen(
    selectedItem: SelectedItem,
    viewModel: ItemDetailViewModel = hiltViewModel(),
    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when(uiState) {
        is ItemDetailsUiState.Error -> ErrorContent(message = "uiState!!.message")
        ItemDetailsUiState.Loading -> LoadingContent()
        is ItemDetailsUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                CardItemComposable((uiState as ItemDetailsUiState.Success).data)
            }
        }
    }
}

@Composable
private fun CardItemComposable(
    item: SelectedItemDetails,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .defaultMinSize(minHeight = 100.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title ?: "-",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
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