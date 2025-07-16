package com.napp.mvvmdemo.ui.main.detailview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.napp.mvvmdemo.ui.navigation.SelectedItem
import com.napp.demoapp.data.item.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: ItemRepository
) : ViewModel() {

    // State flow for retrying the data fetch.
    private val retryFlow = MutableStateFlow(Unit)

    // Get the selected item ID from the saved state handle
    private val selectedItem = savedStateHandle.toRoute<SelectedItem>()

    private val myDataList = flow {
        emit(repository.fetchItemList())
    }

    // StateFlow to hold the UI state
    val uiState: StateFlow<ItemDetailsUiState> = combine(myDataList, retryFlow) { data, _ ->

        ItemDetailsUiState.Success(
            SelectedItemDetails(
                id = selectedItem.id,
                title = "Test data",
                description = "test description"
            )
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ItemDetailsUiState.Loading)

    // Retry the data fetch
    fun retry() {
        retryFlow.value = Unit
    }
}

sealed class ItemDetailsUiState {
    data object Loading : ItemDetailsUiState()
    data class Success(val data: SelectedItemDetails) : ItemDetailsUiState()
    data class Error(val message: String?) : ItemDetailsUiState()
}

/**
 * The selected item details.
 */
data class SelectedItemDetails(
    val id: Int,
    val title: String?,
    val description: String
)