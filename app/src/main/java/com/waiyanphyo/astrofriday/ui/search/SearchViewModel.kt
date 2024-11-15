package com.waiyanphyo.astrofriday.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyanphyo.astrofriday.data.repository.SearchRepository
import com.waiyanphyo.astrofriday.di.IoDispatcher
import com.waiyanphyo.astrofriday.di.MainDispatcher
import com.waiyanphyo.astrofriday.domain.model.Location
import com.waiyanphyo.astrofriday.domain.util.DomainError
import com.waiyanphyo.astrofriday.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _locations = MutableStateFlow<Result<List<Location>, DomainError>?>(null)
    val locations: StateFlow<Result<List<Location>, DomainError>?> get() = _locations

    fun searchWeather(query: String) {
        viewModelScope.launch(ioDispatcher) {
            // Collect data from the repository and emit to _weatherData
            searchRepository.searchLocation(query)
                .collect { result ->
                    withContext(mainDispatcher) {
                        _locations.value = result
                    }
                }
        }
    }
}

