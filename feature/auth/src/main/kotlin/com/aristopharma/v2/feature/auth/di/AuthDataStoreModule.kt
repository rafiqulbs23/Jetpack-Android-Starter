package com.aristopharma.v2.feature.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.aristopharma.v2.core.di.IoDispatcher
import com.aristopharma.v2.feature.auth.data.datasource.local.LoginModelSerializer
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Hilt module for providing DataStore for authentication data.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthDataStoreModule {

    private const val LOGIN_DATA_STORE_FILE_NAME = "login_preferences.json"

    /**
     * Provides the [DataStore] for [LoginModel].
     *
     * @param appContext The application [Context].
     * @param ioDispatcher The [CoroutineDispatcher] for performing I/O operations.
     * @return The [DataStore] for [LoginModel].
     */
    @Singleton
    @Provides
    fun provideLoginModelDataStore(
        @ApplicationContext appContext: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<LoginModel> {
        return DataStoreFactory.create(
            serializer = LoginModelSerializer,
            produceFile = { appContext.dataStoreFile(LOGIN_DATA_STORE_FILE_NAME) },
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        )
    }
}

