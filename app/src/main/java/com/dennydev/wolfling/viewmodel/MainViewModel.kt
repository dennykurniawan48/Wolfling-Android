package com.dennydev.wolfling.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository
): ViewModel() {
    val token = datastoreRepository.flowToken
    val username = datastoreRepository.flowUsername
    private val _selectedBottomNav: MutableState<String> = mutableStateOf(Screen.HomeScreen.route)
    val selecteBottomNav: State<String> = _selectedBottomNav
    private val _isSignedIn = mutableStateOf(false)
    val isSignedIn: State<Boolean> = _isSignedIn

    init {
        viewModelScope.launch {
            datastoreRepository.flowToken.collect { token ->
                _isSignedIn.value = token.isNotEmpty()
            }
        }
    }

    fun onChangeSelectedBottomNav(route: String){
        _selectedBottomNav.value = route
    }
}