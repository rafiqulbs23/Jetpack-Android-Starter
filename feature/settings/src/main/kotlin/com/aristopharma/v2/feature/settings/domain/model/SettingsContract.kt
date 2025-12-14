package com.aristopharma.v2.feature.settings.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsState(
    val settings: Settings = Settings(),
)

@Immutable
sealed interface SettingsEvent {
    data object SignOut : SettingsEvent
}


