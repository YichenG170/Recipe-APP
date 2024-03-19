//EditProfileActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextBio;
    private Spinner spinnerUserLocation;
    private Spinner spinnerUserVisibility;
    private Button btnSaveChanges;
    private String loggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextBio = findViewById(R.id.editTextBio);
        spinnerUserLocation = findViewById(R.id.spinnerUserLocation);
        spinnerUserVisibility = findViewById(R.id.spinnerUserVisibility);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        loggedInUserEmail = getIntent().getStringExtra("userEmail");
        if (loggedInUserEmail != null) {
            loadUserDetailsForEditing(loggedInUserEmail);
        }

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
    }

    private void loadUserDetailsForEditing(String userEmail) {
        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);
        try (Cursor cursor = databaseHelper.getUserDetails(userEmail)) {
            if (cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(databaseHelper.COLUMN_USER_USERNAME);
                int bioIndex = cursor.getColumnIndex(databaseHelper.COLUMN_USER_BIO);
                int locationIndex = cursor.getColumnIndex(databaseHelper.COLUMN_USER_LOCATION);

                // Check if indexes are valid before accessing data
                String username = usernameIndex >= 0 ? cursor.getString(usernameIndex) : "No username found";
                String bio = bioIndex >= 0 ? cursor.getString(bioIndex) : "No bio found";
                String location = locationIndex >= 0 ? cursor.getString(locationIndex) : "Central";

                editTextUsername.setText(username);
                editTextBio.setText(bio);
                ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,R.array.singapore_locations, android.R.layout.simple_spinner_item);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUserLocation.setAdapter(locationAdapter);
                ArrayAdapter<CharSequence> visibilityAdapter = ArrayAdapter.createFromResource(this,R.array.visibility, android.R.layout.simple_spinner_item);
                visibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUserVisibility.setAdapter(visibilityAdapter);
            } else {
                Toast.makeText(EditProfileActivity.this, "Unable to load user details.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(EditProfileActivity.this, "Error loading user details: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveChanges() {
        String newUsername = editTextUsername.getText().toString().trim();
        String newBio = editTextBio.getText().toString().trim();
        String newLocation = spinnerUserLocation.getSelectedItem().toString();
        String newVisibility = spinnerUserVisibility.getSelectedItem().toString();

        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);
        databaseHelper.updateUserProfile(loggedInUserEmail, newUsername, newBio, newLocation, newVisibility);

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        intent.putExtra("loggedInUserEmail", loggedInUserEmail);
        intent.putExtra("viewedUserEmail", loggedInUserEmail);
        startActivity(intent);
        finish();
    }
}
