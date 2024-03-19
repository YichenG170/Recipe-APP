package com.example.recipeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AddressBookDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "addressBook.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_ADDRESS_BOOK = "address_book";
    public static final String COLUMN_OWNER_EMAIL = "owner_email";
    public static final String COLUMN_CONTACT_EMAIL = "contact_email";
    private static final String CREATE_ADDRESS_BOOK_TABLE = "CREATE TABLE " + TABLE_ADDRESS_BOOK + "("
            + COLUMN_OWNER_EMAIL + " TEXT,"
            + COLUMN_CONTACT_EMAIL + " TEXT" + ")";
    public static final String TABLE_CHAT_MESSAGES = "chat_messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_SENDER_EMAIL = "sender_email";
    public static final String COLUMN_RECEIVER_EMAIL = "receiver_email";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String CREATE_CHAT_MESSAGES_TABLE = "CREATE TABLE " + TABLE_CHAT_MESSAGES + "("
            + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SENDER_EMAIL + " TEXT,"
            + COLUMN_RECEIVER_EMAIL + " TEXT,"
            + COLUMN_MESSAGE_TEXT + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    private static final String DROP_ADDRESS_BOOK_TABLE = "DROP TABLE IF EXISTS " + TABLE_ADDRESS_BOOK;
    private final String TAG = "UserInfoDatabaseHelper";

    public AddressBookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADDRESS_BOOK_TABLE);
        db.execSQL(CREATE_CHAT_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);
        onCreate(db);
    }

    public boolean isUserInAddressBook(String ownerEmail, String contactEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_CONTACT_EMAIL};
        String selection = COLUMN_OWNER_EMAIL + " = ?";
        String[] selectionArgs = {ownerEmail};

        Cursor cursor = db.query(TABLE_ADDRESS_BOOK, columns, selection, selectionArgs, null, null, null);
        boolean isInAddressBook = false;

        if (cursor != null && cursor.moveToFirst()) {
            int contactIndex = cursor.getColumnIndex(COLUMN_CONTACT_EMAIL);
            if (contactIndex >= 0) { // Check if the index is valid
                String storedPassword = cursor.getString(contactIndex);
                isInAddressBook = storedPassword.equals(contactEmail);
            }
            cursor.close();
        }

        db.close();
        return isInAddressBook;
    }

    public void addToAddressBook(String ownerEmail, String contactEmail, String contactUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_EMAIL, ownerEmail);
        values.put(COLUMN_CONTACT_EMAIL, contactEmail);
        db.insert(TABLE_ADDRESS_BOOK, null, values);
        db.close();
    }

    public Cursor getAddressBookContacts(String ownerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ADDRESS_BOOK, new String[]{COLUMN_CONTACT_EMAIL},
                COLUMN_OWNER_EMAIL + "=?", new String[]{ownerEmail}, null, null, null);
    }

    public void addChatMessage(String senderEmail, String receiverEmail, String messageText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SENDER_EMAIL, senderEmail);
        values.put(COLUMN_RECEIVER_EMAIL, receiverEmail);
        values.put(COLUMN_MESSAGE_TEXT, messageText);
        db.insert(TABLE_CHAT_MESSAGES, null, values);
        db.close();
    }

    public Cursor getChatHistory(String userOneEmail, String userTwoEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CHAT_MESSAGES +
                " WHERE (" + COLUMN_SENDER_EMAIL + " = ? AND " + COLUMN_RECEIVER_EMAIL + " = ?)" +
                " OR (" + COLUMN_SENDER_EMAIL + " = ? AND " + COLUMN_RECEIVER_EMAIL + " = ?)" +
                " ORDER BY " + COLUMN_TIMESTAMP + " ASC";
        return db.rawQuery(query, new String[]{userOneEmail, userTwoEmail, userTwoEmail, userOneEmail});
    }

    public void updateContactUsername(String contactEmail, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.update(TABLE_ADDRESS_BOOK, values, COLUMN_CONTACT_EMAIL + " = ?", new String[]{contactEmail});
        db.close();
    }

    public Cursor getAllChatContacts(String ownerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all distinct sender emails from chat messages where the receiver is the owner,
        // excluding those already in the address book
        String query = "SELECT DISTINCT " + COLUMN_SENDER_EMAIL + " AS contact_email FROM " + TABLE_CHAT_MESSAGES +
                " WHERE " + COLUMN_RECEIVER_EMAIL + " = ?" +
                " AND " + COLUMN_SENDER_EMAIL + " NOT IN (SELECT " + COLUMN_CONTACT_EMAIL +
                " FROM " + TABLE_ADDRESS_BOOK + " WHERE " + COLUMN_OWNER_EMAIL + " = ?)" +
                " UNION " +
                "SELECT " + COLUMN_CONTACT_EMAIL + " FROM " + TABLE_ADDRESS_BOOK +
                " WHERE " + COLUMN_OWNER_EMAIL + " = ?";

        return db.rawQuery(query, new String[]{ownerEmail, ownerEmail, ownerEmail});
    }

}