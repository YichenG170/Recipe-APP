//MainActivity.Java
package com.example.recipeapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // reset the databases
        /*UserInfoDatabaseHelper UserInfodbHelper = new UserInfoDatabaseHelper(this);
        UserInfodbHelper.resetDatabase();
        AddressBookDatabaseHelper AddressBookdbHelper = new AddressBookDatabaseHelper(this);
        AddressBookdbHelper.resetDatabase();*/

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}
