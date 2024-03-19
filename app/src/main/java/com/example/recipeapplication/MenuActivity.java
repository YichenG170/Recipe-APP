//MenuActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        Button btnRecipePage = findViewById(R.id.btnRecipePage);
        Button btnPantryPage = findViewById(R.id.btnPantryPage);
        Button btnProfilePage = findViewById(R.id.btnProfilePage);
        Button btnFriendSystemPage = findViewById(R.id.btnFriendSystemPage);
        Button btnMessagePage = findViewById(R.id.btnMessagePage);
        Button btnLoginPage = findViewById(R.id.btnLoginPage);

        String loggedInUserEmail = getIntent().getStringExtra("loggedInUserEmail");
        Log.v(TAG, loggedInUserEmail);

        btnRecipePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement Menu Activity
                /*Intent intent = new Intent(MenuActivity.this, RecipeActivity.class);
                startActivity(intent);*/
            }
        });

        btnPantryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement Pantry Activity
                /*Intent intent = new Intent(MenuActivity.this, PantryActivity.class);
                startActivity(intent);*/
            }
        });

        btnProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                intent.putExtra("viewedUserEmail", loggedInUserEmail);
                startActivity(intent);
            }
        });

        btnFriendSystemPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, FriendSystemActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            }
        });

        btnMessagePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SelectUserActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail); // Pass the logged-in user's email
                startActivity(intent);
            }
        });

        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
