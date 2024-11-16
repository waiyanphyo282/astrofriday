package com.waiyanphyo.astrofriday.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyanphyo.astrofriday.data.repository.SearchRepository
import com.waiyanphyo.astrofriday.di.IoDispatcher
import com.waiyanphyo.astrofriday.di.MainDispatcher
import com.waiyanphyo.astrofriday.domain.util.onError
import com.waiyanphyo.astrofriday.domain.util.onSuccess
import com.waiyanphyo.astrofriday.domain.util.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state = _state.asStateFlow()

    fun searchWeather(query: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(ioDispatcher) {
            // Collect data from the repository and emit to _weatherData
            searchRepository.searchLocation(query)
                .collect { result ->
                    withContext(mainDispatcher) {
                        result.onSuccess {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                locations = it,
                                errorMessage = ""
                            )
                        }.onError {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = it.toErrorMessage()
                            )
                        }
                    }
                }
        }
    }
}

