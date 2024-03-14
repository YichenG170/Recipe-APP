package com.example.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.recipe.ui.theme.RecipeTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeTheme {
                // State to control which page to show
                val showLoginPage = remember { mutableStateOf(true) }
                val showMenuPage = remember { mutableStateOf(false) }
                val showUserProfile = remember { mutableStateOf(false) }
                // State to store the current user profile
                val currentUserProfile = remember {
                    mutableStateOf(sampleUserProfile) // TODO: Replace with actual user data
                }
                // State to store error messages
                val errorMessage = remember { mutableStateOf<String?>(null) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showUserProfile.value) {
                        // If the "User Profile" button is clicked, show the UserProfilePage
                        UserProfilePage(
                            currentUserProfile = currentUserProfile.value,
                            onUpdateProfile = { updatedProfile ->
                                // Handle the profile update, e.g., save to database
                                currentUserProfile.value = updatedProfile
                                showUserProfile.value = true
                                showMenuPage.value = false
                            },
                            onReturnToMenu = {
                                // Handle the action to return to the menu
                                showUserProfile.value = false
                                showMenuPage.value = true
                            },
                            isEditable = true // Assume the user can edit their own profile
                        )
                    } else if (showMenuPage.value) {
                        // If the user is logged in, show the menu page
                        MenuPage(
                            onProfileClick = {
                                // When "User Profile" button is clicked
                                showMenuPage.value = false
                                showUserProfile.value = true
                            },
                            onSearchRecipesClick = {
                                // Handle search recipes click
                            },
                            onUpdatePantryClick = {
                                // Handle update pantry click
                            },
                            onFriendSystemClick = {
                                // Handle friend system click
                            },
                            onLogInPageClick = {
                                showMenuPage.value = false
                                showLoginPage.value = true
                                errorMessage.value = null
                            }
                        )
                    } else {
                        // If the user is not logged in, show the login or register page
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Check and display error message if not null
                            errorMessage.value?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            if (showLoginPage.value) {
                                LoginPage(
                                    onLoginSuccess = {
                                        // Hide login page and show menu after successful login
                                        showLoginPage.value = false
                                        showMenuPage.value = true
                                        errorMessage.value = null
                                    },
                                    onLoginFailure = {
                                        // Show error message on login failure
                                        errorMessage.value = "Incorrect Username or Password. Please try again."
                                    }
                                )
                            } else {
                                RegisterPage(
                                    onRegisterSuccess = {
                                        // After successful registration, show login page
                                        showLoginPage.value = true
                                        errorMessage.value = null
                                    },
                                    onRegisterFailure = { message ->
                                        // Show error message on registration failure
                                        errorMessage.value = message
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    // Toggle between login and registration page
                                    errorMessage.value = null
                                    showLoginPage.value = !showLoginPage.value
                                }
                            ) {
                                Text(if (showLoginPage.value) "Register" else "Login")
                            }
                        }
                    }
                }
            }
        }
    }
}