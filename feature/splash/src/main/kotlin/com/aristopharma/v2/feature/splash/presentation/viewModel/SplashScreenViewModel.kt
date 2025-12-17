package com.aristopharma.v2.feature.splash.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<Unit>(replay = 1)
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            // Emit navigation event - navigation will be handled by callback
            _navigationEvent.emit(Unit)
        }
    }
}