package com.waiyanphyo.astrofriday.ui.sport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyanphyo.astrofriday.data.repository.SportRepository
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
class SportViewModel @Inject constructor(
    private val sportRepository: SportRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SportUiState())
    val state = _state.asStateFlow()

    private val _query = MutableLiveData("London")

    private val _events = MutableSharedFlow<SportEvent>(replay = 0)
    val events = _events.asSharedFlow()

    init {
        _query.observeForever {
            getSports()
        }
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    private fun sendErrorEvent(event: SportEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun getSports() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(ioDispatcher) {
            // Collect data from the repository and emit to _weatherData
            sportRepository.getSports(_query.value.toString())
                .collect { result ->
                    withContext(mainDispatcher) {
                        result.onSuccess {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                allSports = it,
                            )
                        }.onError {
                            _state.value = _state.value.copy(isLoading = false)
                            sendErrorEvent(SportEvent.Error(it.toErrorMessage()))
                        }
                    }
                }
        }
    }

}