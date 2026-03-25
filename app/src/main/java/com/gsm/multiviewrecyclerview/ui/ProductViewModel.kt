package com.gsm.multiviewrecyclerview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.multiviewrecyclerview.data.ProductRepository
import com.gsm.multiviewrecyclerview.data.model.UiModel
import com.gsm.multiviewrecyclerview.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _data: MutableStateFlow<UiState<List<UiModel>>> = MutableStateFlow(UiState.Loading)
    val data: StateFlow<UiState<List<UiModel>>> = _data

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            repository.getMainScreenData()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _data.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
                }
                .collect {
                    _data.value = UiState.Success(it)
                }
        }
    }
}