package com.podonin.tradingtask

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SharedViewModel: ViewModel() {

    private val hasSubscriptionKey = booleanPreferencesKey("has_subscription")
    private val _hasSubscriptionFlow = MutableStateFlow(true)
    val hasSubscriptionFlow = _hasSubscriptionFlow.asStateFlow()

    private val _splashCloseFlow = MutableSharedFlow<Float>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val splashCloseFlow = _splashCloseFlow.asSharedFlow()

    fun init(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit {
                it[hasSubscriptionKey] = Random.nextBoolean()
            }
            context.dataStore.data.collect {
//                _hasSubscriptionFlow.value = it[hasSubscriptionKey] ?: false
            }
        }
    }

    fun closeSplashScreen(percent: Float) {
        _splashCloseFlow.tryEmit(percent)
    }
}