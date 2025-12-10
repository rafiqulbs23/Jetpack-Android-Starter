/*
 * Copyright 2025 Md. Rafiqul Islam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aristopharma.v2.feature.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.aristopharma.v2.core.extensions.asOneTimeEvent
import com.aristopharma.v2.core.extensions.stateInDelayed
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateWith
import com.aristopharma.v2.feature.profile.domain.model.ProfileState
import com.aristopharma.v2.feature.profile.domain.usecase.GetProfileUseCase
import com.aristopharma.v2.feature.profile.domain.usecase.SignOutProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * [ViewModel] for [ProfileScreen].
 *
 * @param profileRepository [ProfileRepository].
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val signOutProfileUseCase: SignOutProfileUseCase,
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(UiState(ProfileState()))
    val profileUiState = _profileUiState
        .onStart { updateProfileData() }
        .stateInDelayed(UiState(ProfileState()), viewModelScope)

    private fun updateProfileData() {
        getProfileUseCase()
            .map { profile -> UiState(ProfileState(profile)) }
            .onEach { data -> _profileUiState.update { data } }
            .catch { e -> UiState(ProfileState(), error = e.asOneTimeEvent()) }
            .launchIn(viewModelScope)
    }

    fun signOut() {
        _profileUiState.updateWith { signOutProfileUseCase() }
    }
}
