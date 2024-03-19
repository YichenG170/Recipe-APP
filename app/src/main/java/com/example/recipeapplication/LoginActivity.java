//LoginActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.security.NoSuchAlgorithmException;
import android.widget.Toast;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private String loggedInUserEmail; // store the user logged in for identification purpose
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        textViewRegister = findViewById(R.id.tvRegister);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Hash the input password
        String hashedPassword = Hashing.hashPassword(password);

        UserInfoDatabaseHelper databaseHelper = new UserInfoDatabaseHelper(LoginActivity.this);
        // Check user with hashed password
        if (databaseHelper.checkUser(email, hashedPassword)) {
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            //Toast.makeText(LoginActivity.this, loggedInUserEmail, Toast.LENGTH_SHORT).show();
            setLoggedInUserEmail(email);
            Log.v(TAG,email);
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.putExtra("loggedInUserEmail", email);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Login failed. Invalid email/password", Toast.LENGTH_SHORT).show();
        }
    }

    public String getLoggedInUserEmail() {
        return this.loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String a) {
        this.loggedInUserEmail = a;
    }
}
