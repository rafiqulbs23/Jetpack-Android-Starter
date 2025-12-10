package com.aristopharma.v2.feature.profile.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileState(
    val profile: Profile = Profile(userName = "", profilePictureUri = null),
)

@Immutable
sealed interface ProfileEvent {
    data object SignOut : ProfileEvent
}

