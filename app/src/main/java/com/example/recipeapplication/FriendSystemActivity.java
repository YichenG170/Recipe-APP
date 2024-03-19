//FriendSystemActivity.java
package com.example.recipeapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

public class FriendSystemActivity extends AppCompatActivity {
    private String loggedInUserEmail;
    private static final String TAG = "FriendSystemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_system);

        loggedInUserEmail = getIntent().getStringExtra("loggedInUserEmail");

        Button btnSearchUserByEmail = findViewById(R.id.btnSearchUserByEmail);
        Button btnShowUsersNearby = findViewById(R.id.btnShowUsersNearby);
        Button btnMenuPage = findViewById(R.id.btnMenuPage);

        btnSearchUserByEmail.setOnClickListener(view -> {
            promptForUserEmail();
        });

        btnShowUsersNearby.setOnClickListener(view -> {
            showUsersNearby();
        });

        btnMenuPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendSystemActivity.this, MenuActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            }
        });
    }

    private void showUsersNearby() {
        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);
        String userLocation = null;

        Cursor cursor = databaseHelper.getUserDetails(loggedInUserEmail);
        if (cursor != null && cursor.moveToFirst()) {
            userLocation = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_USER_LOCATION));
            cursor.close();
        }
        Log.v(TAG, cursor.toString());
        Log.v(TAG,userLocation);
        if (userLocation != null) {
            Cursor usersNearbyCursor = databaseHelper.getUsersNearby(userLocation, loggedInUserEmail);

            if (usersNearbyCursor != null && usersNearbyCursor.moveToFirst()) {
                // Assuming COLUMN_USER_EMAIL is the column name for the user email
                String randomUserEmail = usersNearbyCursor.getString(usersNearbyCursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_USER_EMAIL));
                usersNearbyCursor.close();

                // Start ProfileActivity to show the selected user's profile
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("viewedUserEmail", randomUserEmail);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No users nearby.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Your location is unknown. Please update your profile.", Toast.LENGTH_LONG).show();
        }
    }

    private void promptForUserEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search User by Email");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                searchUser(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void searchUser(String email) {
        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);
        Cursor cursor = databaseHelper.getUserDetails(email);

        if (cursor != null && cursor.moveToFirst()) {
            String visibility = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_PROFILE_VISIBILITY));
            cursor.close();

            if ("Visible".equals(visibility)) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("viewedUserEmail", email);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            } else {
                // User's profile is not visible
                Toast.makeText(this, "This user's profile is not visible.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
