package com.aristopharma.v2.feature.home.domain.model

import androidx.compose.runtime.Immutable
import com.aristopharma.v2.core.utils.OneTimeEvent
import java.util.UUID

@Immutable
data class HomeState(
    val jetpacks: List<Jetpack> = emptyList(),
)

@Immutable
data class ItemState(
    val jetpackId: String = UUID.randomUUID().toString(),
    val jetpackName: String = "",
    val jetpackPrice: Double = 0.0,
    val navigateBack: OneTimeEvent<Boolean> = OneTimeEvent(false),
)

@Immutable
sealed interface HomeEvent {
    data class Delete(val jetpack: Jetpack) : HomeEvent
}

@Immutable
sealed interface ItemEvent {
    data object Save : ItemEvent
}

