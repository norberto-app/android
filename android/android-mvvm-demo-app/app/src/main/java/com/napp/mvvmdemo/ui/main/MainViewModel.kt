package com.napp.mvvmdemo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napp.demoapp.data.common.DataResponse
import com.napp.demoapp.data.item.ItemListResult
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
class MainViewModel @Inject constructor(
    val repository: ItemRepository
) : ViewModel() {

    // State flow for retrying the data fetch.
    private val retryFlow = MutableStateFlow<Long>(0)

    private val myDataList = flow {
        emit(repository.fetchItemList())
    }

    // StateFlow to hold the UI state
    val uiState: StateFlow<MainUiState> = combine(myDataList, retryFlow) { data, _ ->
        if (data is DataResponse.Success<ItemListResult>) {
            MainUiState.Success(
                items = data.result.items.map {
                    MainListItemUiState(
                        id = it.id,
                        title = it.title ?: "",
                        description = it.description ?: ""
                    )
                }
            )
        } else {
            MainUiState.Error(message = (data as DataResponse.Error<*>).message)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MainUiState.Loading)

    // Retry the data fetch
    fun retry() {
        retryFlow.value = System.currentTimeMillis()
    }
}
