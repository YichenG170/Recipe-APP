//MessageActivity.java
package com.example.recipeapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private ListView lvChatHistory;
    private EditText etMessage;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private AddressBookDatabaseHelper dbHelper;
    private String ownerEmail, contactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        lvChatHistory = findViewById(R.id.lvChatHistory);
        etMessage = findViewById(R.id.etMessage);
        dbHelper = new AddressBookDatabaseHelper(this);

        ownerEmail = getIntent().getStringExtra("ownerEmail");
        contactEmail = getIntent().getStringExtra("contactEmail");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
        lvChatHistory.setAdapter(adapter);

        loadChatHistory();

        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    dbHelper.addChatMessage(ownerEmail, contactEmail, messageText);
                    chatMessages.add("Me: " + messageText);
                    adapter.notifyDataSetChanged();
                    etMessage.setText("");
                    // Optionally, scroll to the bottom
                    lvChatHistory.setSelection(chatMessages.size() - 1);
                }
            }
        });

        Button btnViewProfile = findViewById(R.id.btnViewProfile);

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, ProfileActivity.class);
                intent.putExtra("loggedInUserEmail", ownerEmail);
                intent.putExtra("viewedUserEmail", contactEmail);
                startActivity(intent);
            }
        });

        Button btnChooseAnotherUser = findViewById(R.id.btnChooseAnotherUser);

        btnChooseAnotherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use CLEAR_TOP flag to clear the activity stack up to SelectUserActivity
                Intent intent = new Intent(MessageActivity.this, SelectUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("loggedInUserEmail", ownerEmail); // Pass back the owner's email to keep context
                startActivity(intent);
            }
        });
    }

    private void loadChatHistory() {
        Cursor cursor = dbHelper.getChatHistory(ownerEmail, contactEmail);
        if (cursor.moveToFirst()) {
            int senderIndex = cursor.getColumnIndex(AddressBookDatabaseHelper.COLUMN_SENDER_EMAIL);
            int messageIndex = cursor.getColumnIndex(AddressBookDatabaseHelper.COLUMN_MESSAGE_TEXT);

            // Check if columns exist
            if (senderIndex >= 0 && messageIndex >= 0) {
                do {
                    String sender = cursor.getString(senderIndex);
                    String messageText = cursor.getString(messageIndex);
                    if (sender.equals(ownerEmail)) {
                        chatMessages.add("Me: " + messageText);
                    } else {
                        chatMessages.add(contactEmail + ": " + messageText);
                    }
                } while (cursor.moveToNext());
            }
            adapter.notifyDataSetChanged();
        }
        cursor.close();
    }

}
