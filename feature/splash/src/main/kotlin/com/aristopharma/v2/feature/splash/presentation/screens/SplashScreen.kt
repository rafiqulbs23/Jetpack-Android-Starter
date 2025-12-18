package com.aristopharma.v2.feature.splash.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aristopharma.v2.core.di.NavigationEntryPoint
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.feature.splash.R
import com.aristopharma.v2.feature.splash.presentation.viewModel.SplashScreenViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    viewModel: SplashScreenViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            // Get UserPreferencesDataSource from Hilt using centralized NavigationEntryPoint
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                NavigationEntryPoint::class.java
            )
            val userPreferencesDataSource = entryPoint.userPreferencesDataSource()
            
            // Check if user is logged in
            val isLoggedIn = withContext(Dispatchers.IO) {
                try {
                    val userId = userPreferencesDataSource.getUserIdOrThrow()
                    userId.isNotEmpty()
                } catch (e: IllegalStateException) {
                    false
                }
            }
            
            if (isLoggedIn) {
                onNavigateToDashboard()
            } else {
                onNavigateToSignIn()
            }
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_aristo_logo),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(96.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.aristopharma_msfa),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
        )
    }
}
