package com.waiyanphyo.astrofriday.ui.astronomy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyanphyo.astrofriday.data.repository.AstronomyRepository
import com.waiyanphyo.astrofriday.di.IoDispatcher
import com.waiyanphyo.astrofriday.di.MainDispatcher
import com.waiyanphyo.astrofriday.domain.util.onError
import com.waiyanphyo.astrofriday.domain.util.onSuccess
import com.waiyanphyo.astrofriday.domain.util.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AstronomyViewModel @Inject constructor(
    private val astronomyRepository: AstronomyRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(AstronomyUiState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<AstronomyEvent>(replay = 0)
    val events = _events.asSharedFlow()

    private fun sendErrorEvent(message: String) {
        viewModelScope.launch {
            _events.emit(AstronomyEvent.Error(message))
        }
    }

    fun loadAstronomy(query: String, date: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(ioDispatcher) {
            astronomyRepository.getAstronomy(query, date)
                .collect { result ->
                    withContext(mainDispatcher) {
                        result.onSuccess {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                astronomy = it
                            )
                        }.onError {
                            _state.value = _state.value.copy(isLoading = false)
                            sendErrorEvent(it.toErrorMessage())
                        }
                    }
                }
        }
    }
}