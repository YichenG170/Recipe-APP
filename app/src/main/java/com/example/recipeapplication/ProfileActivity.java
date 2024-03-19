//ProfileActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.util.Log;

public class ProfileActivity extends AppCompatActivity {
    private TextView textViewUsername;
    private TextView textViewBio;
    private TextView textViewLocation;
    private String loggedInUserEmail;
    private String viewedUserEmail;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewBio = findViewById(R.id.textViewBio);
        textViewLocation = findViewById(R.id.textViewLocation);

        loggedInUserEmail = getIntent().getStringExtra("loggedInUserEmail");
        viewedUserEmail = getIntent().getStringExtra("viewedUserEmail");

        Log.d(TAG, "Starting ProfileActivity with viewedUserEmail: " + viewedUserEmail);
        Log.d(TAG, "Starting ProfileActivity with viewedUserEmail: " + loggedInUserEmail);

        if (viewedUserEmail != null) {
            loadUserProfile(viewedUserEmail);
        } else {
            Toast.makeText(this, "User email is null", Toast.LENGTH_SHORT).show();
        }

        Button btnMenuPage = findViewById(R.id.btnMenuPage);

        btnMenuPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            }
        });

        Button btnAddToAddressBook = findViewById(R.id.btnAddToAddressBook);

        AddressBookDatabaseHelper addressBookDatabaseHelper = new AddressBookDatabaseHelper(this);

        if (!viewedUserEmail.equals(loggedInUserEmail) &&
                !addressBookDatabaseHelper.isUserInAddressBook(loggedInUserEmail, viewedUserEmail)) {

            btnAddToAddressBook.setVisibility(View.VISIBLE);
            btnAddToAddressBook.setOnClickListener(v -> {
                addressBookDatabaseHelper.addToAddressBook(loggedInUserEmail, viewedUserEmail, textViewUsername.getText().toString());
                Toast.makeText(ProfileActivity.this, "Added to address book", Toast.LENGTH_SHORT).show();
                btnAddToAddressBook.setVisibility(View.GONE); // Optionally hide the button after adding
            });
        } else {
            btnAddToAddressBook.setVisibility(View.GONE);
        }

        Button btnMessage = findViewById(R.id.btnMessage);

        if (!viewedUserEmail.equals(loggedInUserEmail) &&
                addressBookDatabaseHelper.isUserInAddressBook(loggedInUserEmail, viewedUserEmail)) {

            btnMessage.setVisibility(View.VISIBLE);
            btnMessage.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MessageActivity.class);
                intent.putExtra("ownerEmail", loggedInUserEmail); // The user who is logged in
                intent.putExtra("contactEmail", viewedUserEmail); // The email of the profile being viewed
                startActivity(intent);
                Log.v(TAG,"1");
            });
        } else {
            btnMessage.setVisibility(View.GONE);
        }
    }

    private void loadUserProfile(String userEmail) {
        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);
        Cursor cursor = databaseHelper.getUserDetails(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_USER_USERNAME));
            String bio = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_USER_BIO));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoDatabaseHelper.COLUMN_USER_LOCATION));

            textViewUsername.setText((username != null && !username.isEmpty()) ? username : "Default username (email)");
            textViewBio.setText((bio != null && !bio.isEmpty()) ? bio : "This is a default bio.");
            textViewLocation.setText((location != null && !location.isEmpty()) ? location : "Singapore");
        } else {
            Log.d(TAG, "User details not found for email: " + userEmail);
            Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        if (viewedUserEmail.equals(loggedInUserEmail)) {
            Button editProfileButton = findViewById(R.id.editProfileButton);
            editProfileButton.setVisibility(View.VISIBLE);
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start EditProfileActivity or show editable fields
                    Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    editIntent.putExtra("userEmail", userEmail); // Pass the email to EditProfileActivity
                    startActivity(editIntent);
                    Toast.makeText(ProfileActivity.this, "Edit button clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Button editProfileButton = findViewById(R.id.editProfileButton);
            editProfileButton.setVisibility(View.INVISIBLE);
        }
    }
}
