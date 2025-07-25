package com.napp.mvvmdemo.ui.main

/**
 * This is the state that represents what the screen will look like
 */
sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(val items: List<MainListItemUiState>) : MainUiState()
    data class Error(val message: String?) : MainUiState()
}

/**
 * This represents an item on the list
 */
data class MainListItemUiState(
    val id: Int,
    val title: String?,
    val description: String?
)