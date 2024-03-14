// MenuPage.kt
package com.example.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuPage(
    onProfileClick: () -> Unit,
    onSearchRecipesClick: () -> Unit,
    onUpdatePantryClick: () -> Unit,
    onFriendSystemClick: () -> Unit,
    onLogInPageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Profile button
        Button(
            onClick = onProfileClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("User Profile")
        }

        // Row for View Recipes and Update Pantry buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onSearchRecipesClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("View Recipes")
            }
            Button(
                onClick = onUpdatePantryClick,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("Update Pantry")
            }
        }

        // Friend System button
        Button(
            onClick = onFriendSystemClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Friend System")
        }

        // Log Out button
        Button(
            onClick = onLogInPageClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}
