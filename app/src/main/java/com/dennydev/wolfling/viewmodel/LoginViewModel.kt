package com.dennydev.wolfling.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.login.Login
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import com.dennydev.wolfling.repository.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: LoginRepository,
    val dataStoreRepository: AuthStoreRepository
): ViewModel() {
    private val _username = mutableStateOf("")
    val username: State<String> = _username
    private val _password = mutableStateOf("")
    val password: State<String> = _password
    private val _validFormInput = mutableStateOf(false)
    val validFormInput: State<Boolean> = _validFormInput
    private val _errorUsername = mutableStateOf("")
    val errorUsername: State<String> = _errorUsername
    private val _errorPassword = mutableStateOf("")
    val errorPassword: State<String> = _errorPassword
    private val _showPassword =  mutableStateOf(false)
    val showPassword: State<Boolean> = _showPassword

    private val _isSignedIn = mutableStateOf(false)
    val isSignedIn: State<Boolean> = _isSignedIn

    init {
        viewModelScope.launch {
            dataStoreRepository.flowToken.collect { token ->
                _isSignedIn.value = token.isNotEmpty()
            }
        }
    }

    private val _isGoogleLogIn = mutableStateOf(false)
    val isGoogleLogin: State<Boolean> = _isGoogleLogIn

    private val _loginResponse = mutableStateOf<ApiResponse<Login>>(ApiResponse.Idle())
    val loginResponse: State<ApiResponse<Login>> = _loginResponse

    fun changeShowPassword(){
        _showPassword.value = !showPassword.value
    }

    fun changeGoogleLogin(isGoogleLogin: Boolean){
        _isGoogleLogIn.value = isGoogleLogin
    }

    fun changeUsername(newUsername: String){
        if(newUsername.isEmpty()){
            _errorUsername.value = "Username is empty"
        }else{
            _errorUsername.value = ""
        }
        checkValidForm()
        _username.value = newUsername
    }

    fun changePassword(newPassword: String){
        if(newPassword.isEmpty()){
            _errorUsername.value = "Password is empty"
        }else{
            _errorUsername.value = ""
        }
        checkValidForm()
        _password.value = newPassword
    }

    fun checkValidForm(){
        _validFormInput.value = _password.value.isNotEmpty() && _username.value.isNotEmpty()
    }

    fun loginCredentials(){
        _loginResponse.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.login(_username.value.trim(), _password.value.trim())
            if(response is ApiResponse.Success){
                _loginResponse.value = response
                response.data?.data?.let {
                    dataStoreRepository.saveToken(token = it.accessToken, exp = it.expiresIn, google = it.google, _username=it.username ?: "", _userId = it.id)
                    _isSignedIn.value = true
                }
            }
            _loginResponse.value = response
        }
    }

    fun loginGoogle(token: String){
        _loginResponse.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = repository.loginGoogle(token)
            if(response is ApiResponse.Success){
                _loginResponse.value = response
                response.data?.data?.let {
                    dataStoreRepository.saveToken(token = it.accessToken, exp = it.expiresIn, google = it.google, _username=it.username ?: "", _userId = it.id)
                    _isSignedIn.value = true
                }
            }
            _loginResponse.value = response
        }
    }
}