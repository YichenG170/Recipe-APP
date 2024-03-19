//SelectUserActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;

public class SelectUserActivity extends AppCompatActivity {
    ListView listViewUsers;
    ArrayList<String> usersList = new ArrayList<>();
    ArrayList<String> userEmailsList = new ArrayList<>();
    AddressBookDatabaseHelper addressBookDatabaseHelper;
    String loggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        loggedInUserEmail = getIntent().getStringExtra("loggedInUserEmail");

        listViewUsers = findViewById(R.id.listViewUsers);
        addressBookDatabaseHelper = new AddressBookDatabaseHelper(this);

        populateUsersList();

        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUserEmail = userEmailsList.get(position);
            Intent intent = new Intent(SelectUserActivity.this, MessageActivity.class);
            intent.putExtra("contactEmail", selectedUserEmail);
            intent.putExtra("ownerEmail", loggedInUserEmail);
            startActivity(intent);
        });

        Button btnMenuPage = findViewById(R.id.btnMenuPage);

        btnMenuPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, MenuActivity.class);
                intent.putExtra("loggedInUserEmail", loggedInUserEmail);
                startActivity(intent);
            }
        });
    }

    private void populateUsersList() {
        loggedInUserEmail = getIntent().getStringExtra("loggedInUserEmail");
        Cursor addressBookCursor = addressBookDatabaseHelper.getAddressBookContacts(loggedInUserEmail);
        UserInfoDatabaseHelper userInfoDbHelper = new UserInfoDatabaseHelper(this);

        // Keep track of emails already added to avoid duplicates
        HashSet<String> addedEmails = new HashSet<>();

        // First, add users from the address book
        if (addressBookCursor != null) {
            while (addressBookCursor.moveToNext()) {
                String email = addressBookCursor.getString(addressBookCursor.getColumnIndexOrThrow(AddressBookDatabaseHelper.COLUMN_CONTACT_EMAIL));
                if (!addedEmails.contains(email)) {
                    addUserToList(email, userInfoDbHelper, true);
                    addedEmails.add(email);
                }
            }
            addressBookCursor.close();
        }

        // Then, add users who have sent messages but are not in the address book
        Cursor chatCursor = addressBookDatabaseHelper.getAllChatContacts(loggedInUserEmail);
        if (chatCursor != null) {
            while (chatCursor.moveToNext()) {
                String email = chatCursor.getString(chatCursor.getColumnIndexOrThrow(AddressBookDatabaseHelper.COLUMN_CONTACT_EMAIL));
                if (!addedEmails.contains(email)) {
                    addUserToList(email, userInfoDbHelper, false);
                    addedEmails.add(email);
                }
            }
            chatCursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        listViewUsers.setAdapter(adapter);
    }

    private void addUserToList(String email, UserInfoDatabaseHelper userInfoDbHelper, Boolean inAddressBook) {
        Cursor userCursor = userInfoDbHelper.getUserDetails(email);
        if (userCursor != null && userCursor.moveToFirst()) {
            int usernameIndex = userCursor.getColumnIndex(UserInfoDatabaseHelper.COLUMN_USER_USERNAME);
            if(usernameIndex >= 0) {
                String username = userCursor.getString(usernameIndex);
                if (inAddressBook) {
                    usersList.add(username);
                } else {
                    usersList.add("[Not in Address Book] " + username);
                }
            } else {
                usersList.add("Unknown");
            }
        } else {
            usersList.add("Unknown");
        }
        if (userCursor != null) {
            userCursor.close();
        }
        userEmailsList.add(email); // For passing to the MessageActivity
    }
}
