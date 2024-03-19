//RegisterActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);
        buttonRegister = findViewById(R.id.btnRegister);
        textViewLogin = findViewById(R.id.tvLogin);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (password.length() > 12) {
            Toast.makeText(this, "Password should not exceed 12 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(this);

        // Check if user already exists
        if (!databaseHelper.checkUser(email,password)) {
            // No existing user, proceed with registration
            // TODO: verify the email
            String hashedPassword = Hashing.hashPassword(password); // Hash the password
            databaseHelper.addUser(email, hashedPassword); // Use hashed password
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            Intent editIntent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            editIntent.putExtra("userEmail", email); // Pass the email to EditProfileActivity
            startActivity(editIntent);
        } else {
            // User exists, show error
            Toast.makeText(this, "User already exists with this email", Toast.LENGTH_SHORT).show();
        }
    }
}
