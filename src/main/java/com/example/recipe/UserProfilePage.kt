// UserProfile.kt
package com.example.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import com.example.recipe.ui.theme.RecipeTheme

// Assuming a data class for the user profile
data class UserProfile(
    val username: String,
    val introduction: String,
    val location: String,
    val isProfileVisible: Boolean
)

// Composable function for the user profile page
@Composable
fun UserProfilePage(
    currentUserProfile: UserProfile,
    onUpdateProfile: (UserProfile) -> Unit,
    onReturnToMenu: () -> Unit,
    isEditable: Boolean
) {
    var username by remember { mutableStateOf(currentUserProfile.username) }
    var introduction by remember { mutableStateOf(currentUserProfile.introduction) }
    var location by remember { mutableStateOf(currentUserProfile.location) }
    var isProfileVisible by remember { mutableStateOf(currentUserProfile.isProfileVisible) }
    var isInEditMode by remember { mutableStateOf(false) }

    RecipeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile information
                Text(text = "Username: $username", style = MaterialTheme.typography.titleLarge)
                Text(text = "Introduction: $introduction", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Location: $location", style = MaterialTheme.typography.bodyLarge)
                Text(text = if (isProfileVisible) "Visibility: Public" else "Visibility: Private")

                // Edit Button (only visible if isEditable is true)
                if (isEditable && !isInEditMode) {
                    Button(onClick = { isInEditMode = true }) {
                        Text("Edit")
                    }
                }

                // Editable fields (only visible in edit mode)
                if (isInEditMode) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Edit Username") }
                    )
                    TextField(
                        value = introduction,
                        onValueChange = { introduction = it },
                        label = { Text("Edit Introduction") }
                    )
                    TextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Edit Location") }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isProfileVisible,
                            onCheckedChange = { isProfileVisible = it }
                        )
                        Text(text = if (isProfileVisible) "Profile is public" else "Profile is private")
                    }

                    // Save Changes Button
                    Button(
                        onClick = {
                            onUpdateProfile(UserProfile(username, introduction, location, isProfileVisible))
                            isInEditMode = false // Exit edit mode after saving changes
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Save Changes")
                    }
                }

                // Return to Menu Button
                Button(
                    onClick = onReturnToMenu,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text("Return to Menu")
                }
            }
        }
    }
}

// Sample user profile with placeholders for presentation
val sampleUserProfile = UserProfile(
    username = "SampleUser",
    introduction = "This is a sample introduction.",
    location = "Sample Location",
    isProfileVisible = true
)

