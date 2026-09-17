package com.example.checkinn_android.ui.checkinns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.usecase.GetBookingOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckinnsListViewModel @Inject constructor(
    private val getBookingOrdersUseCase: GetBookingOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckinnsListUiState())
    val uiState: StateFlow<CheckinnsListUiState> = _uiState.asStateFlow()

    fun onEvent(event: CheckinnsListUiEvent) {
        when (event) {
            is CheckinnsListUiEvent.Refresh -> loadBookingOrders()
        }
    }

    fun loadBookingOrders() {
        viewModelScope.launch {
            getBookingOrdersUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                orders = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
