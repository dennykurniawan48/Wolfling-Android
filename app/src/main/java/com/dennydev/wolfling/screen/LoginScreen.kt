package com.dennydev.wolfling.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dennydev.wolfling.R
import com.dennydev.wolfling.component.login.GoogleSignInHelper
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val username by loginViewModel.username
    val password by loginViewModel.password

    val errorPassword by loginViewModel.errorPassword
    val errorUsername by loginViewModel.errorUsername

    val showPassword by loginViewModel.showPassword
    val isGoogleLogin by loginViewModel.isGoogleLogin

    val loginResponse by loginViewModel.loginResponse

    val validForm by loginViewModel.validFormInput
    val isSignedIn by loginViewModel.isSignedIn

    LaunchedEffect(key1 = isSignedIn){
        if(isSignedIn){
            navController.navigate(Screen.HomeScreen.route){
                popUpTo(Screen.HomeScreen.route){
                    inclusive=true
                }
            }
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Login") }) }) {it ->
        GoogleSignInHelper(context = context, onGoogleSignInSeleced = isGoogleLogin, onDismiss = {loginViewModel.changeGoogleLogin(false)}, onResultReceived = {
                token -> loginViewModel.loginGoogle(token)
        })
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(it)
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Email", style = MaterialTheme.typography.labelSmall)
                Text(errorUsername, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = username, onValueChange = {
                loginViewModel.changeUsername(it)
            }, placeholder = {Text("Username or Email")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Enter Username")
            }, modifier=Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Password", style = MaterialTheme.typography.labelSmall)
                Text(errorPassword, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = {
                loginViewModel.changePassword(it)
            }, placeholder = {Text("Enter password")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Enter Password")
            }, modifier=Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = {
                    loginViewModel.changeShowPassword()
                }) {
                    Icon(imageVector = if(showPassword)  Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                }
            }, visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if(loginResponse is ApiResponse.Error){
                Spacer(modifier = Modifier.height(24.dp))
                Text(loginResponse.message.toString(), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(loginResponse !is ApiResponse.Loading) {
                    loginViewModel.loginCredentials()
                }
            },modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp), enabled = validForm) {
                Text(if(loginResponse is ApiResponse.Loading) "Loading..." else "Login", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Or")
            }
            if(loginResponse is ApiResponse.Error){
                Spacer(modifier = Modifier.height(24.dp))
                Text(loginResponse.message.toString(), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = {
                if(loginResponse !is ApiResponse.Loading) {
                    loginViewModel.changeGoogleLogin(true)
                }
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier=Modifier.width(8.dp))
                    Text(if(loginResponse is ApiResponse.Loading) "Loading..." else "Login with Google")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = {
                navController.navigate(Screen.RegisterScreen.route)
            }) {
                Text("Dont have an account? Register")
            }
        }
    }
}