package com.dennydev.wolfling.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel= hiltViewModel()) {
    val name by registerViewModel.name
    val email by registerViewModel.email
    val password by registerViewModel.password
    val validForm by registerViewModel.validFormInput
    val errorName by registerViewModel.errorName
    val errorEmail by registerViewModel.errorEmail
    val errorPassword by registerViewModel.errorPassword
    val registerResponse by registerViewModel.registerResponse
    val showPassword by registerViewModel.showPassword

    LaunchedEffect(key1 = registerResponse){
        if(registerResponse is ApiResponse.Success){
            navController.popBackStack()
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title={Text("Register")}, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back")
            }
        })
    }) {it ->
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(it)
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Name", style = MaterialTheme.typography.labelSmall)
                Text(errorName, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = name, onValueChange = {
                registerViewModel.changeName(it)
            }, placeholder = { Text("Enter your name") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Enter your name")
            }, modifier= Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Email", style = MaterialTheme.typography.labelSmall)
                Text(errorEmail, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = {
                registerViewModel.changeEmail(it)
            }, placeholder = { Text("Enter email") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Enter Email")
            }, modifier= Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Password", style = MaterialTheme.typography.labelSmall)
                Text(errorPassword, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = {
                registerViewModel.changePassword(it)
            }, placeholder = { Text("Enter password") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Enter Password")
            }, modifier= Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = { registerViewModel.changeShowPassword() }) {
                    Icon(imageVector = if(showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                }
            }, visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if(registerResponse is ApiResponse.Error){
                Spacer(modifier = Modifier.height(24.dp))
                Text(registerResponse.message.toString(), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(!(registerResponse is ApiResponse.Loading)){
                    registerViewModel.register()
                }
            }, modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp), enabled = validForm) {
                Text(if(registerResponse is ApiResponse.Loading)  "Loading" else "Register", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? Login")
            }
        }
    }
}