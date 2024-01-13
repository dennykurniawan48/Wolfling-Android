package com.dennydev.wolfling.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.register.Register
import com.dennydev.wolfling.repository.login.LoginRepository
import com.dennydev.wolfling.repository.register.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val repository: RegisterRepository
): ViewModel() {
    private val _name = mutableStateOf("")
    val name: State<String> = _name
    private val _email = mutableStateOf("")
    val email: State<String> = _email
    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _errorEmail = mutableStateOf("")
    val errorEmail: State<String> = _errorEmail
    private val _errorName = mutableStateOf("")
    val errorName: State<String> = _errorName
    private val _errorPassword = mutableStateOf("")
    val errorPassword: State<String> = _errorPassword
    private val _showPassword = mutableStateOf(false)
    val showPassword: State<Boolean> = _showPassword
    private val _validFormInput = mutableStateOf(false)
    val validFormInput: State<Boolean> = _validFormInput

    private val touchedName = mutableStateOf(false)
    private val touchedEmail = mutableStateOf(false)
    private val touchedPassword = mutableStateOf(false)

    private val _registerResponse = mutableStateOf<ApiResponse<Register>>(ApiResponse.Idle())
    val registerResponse: State<ApiResponse<Register>> = _registerResponse

    val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

    fun checkValidForm(){
        _validFormInput.value = _errorName.value.isEmpty() &&
                _errorEmail.value.isEmpty() &&
                _errorPassword.value.isEmpty() &&
                touchedName.value &&
                touchedEmail.value &&
                touchedPassword.value
    }

    fun changeShowPassword(){
        _showPassword.value = !_showPassword.value
    }

    fun changeName(newName: String){
        touchedName.value = true
        _errorName.value = ""
        if(newName.trim().length < 4){
            _errorName.value = "Name min 4 characters"
        }
        if(newName.isEmpty()){
            _errorName.value = "Can't empty."
        }
        checkValidForm()
        if(newName.length > 20) {
            _errorName.value = "Name max 20 characters"
        }
        _name.value = newName
    }

    fun changeEmail(newEmail: String){
        touchedEmail.value = true
        _errorEmail.value = ""
        if(!emailRegex.matches(newEmail)) {
            _errorEmail.value = "Email isn't valid."
        }
        if(newEmail.isEmpty()){
            _errorEmail.value = "Can't empty."
        }
        checkValidForm()
        _email.value = newEmail.lowercase()
    }

    fun changePassword(newPassword: String){
        touchedPassword.value = true
        _errorPassword.value = ""
        if(newPassword.trim().length < 6){
            _errorPassword.value = "Pass min 6 characters"
        }
        if(newPassword.isEmpty()){
            _errorPassword.value = "Can't empty."
        }
        checkValidForm()
        if(newPassword.length > 15) {
            _errorPassword.value = "Pass max 15 characters"
        }
        _password.value = newPassword
    }

    fun register(){
        viewModelScope.launch(Dispatchers.IO) {
            _registerResponse.value = repository.register(_name.value.trim(), _email.value.trim(), _password.value.trim())
        }
    }
}