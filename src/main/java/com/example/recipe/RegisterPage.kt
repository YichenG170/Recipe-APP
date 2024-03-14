// RegisterPage.kt
package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun RegisterPage(
    onRegisterSuccess: () -> Unit,
    onRegisterFailure: (String) -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it
                errorMessage.value = null
            },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = errorMessage.value != null
        )
        if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                errorMessage.value = null
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = errorMessage.value != null
        )
        Button(
            onClick = {
                // TODO: check if the user exists
                when {
                    username.value.length !in 6..24 -> onRegisterFailure("Username must be between 6 and 24 characters")
                    password.value.length !in 1..64 -> onRegisterFailure("Password must be between 1 and 64 characters")
                    doesUsernameExist(username.value) -> onRegisterFailure("Username already exists")
                    else -> {
                        createNewUser(username.value, password.value)
                        onRegisterSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}

fun doesUsernameExist(username: String): Boolean {
    // TODO: replace with actual database check
    return false
}

fun createNewUser(username: String, password: String) {
    // TODO: replace with actual user creation logic
    println("User created with username: $username and password: $password")
}
