//UserInfoDatabaseHelper.java
package com.example.recipeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserInfoDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_USERNAME = "user_username";
    public static final String COLUMN_USER_BIO = "user_bio";
    public static final String COLUMN_USER_LOCATION = "user_location";
    public static final String COLUMN_PROFILE_VISIBILITY = "profile_visibility";
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private final String TAG = "UserInfoDatabaseHelper";
    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_USERNAME + " TEXT,"
            + COLUMN_USER_BIO + " TEXT,"
            + COLUMN_USER_LOCATION + " TEXT DEFAULT 'Central',"
            + COLUMN_PROFILE_VISIBILITY + " INTEGER DEFAULT 'Visible'"
            + ")";

    public UserInfoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_USER_TABLE); // Drop the user table
        onCreate(db); // Recreate the database
    }

    public void addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean checkUser(String email, String hashedPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_PASSWORD};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        boolean loginSuccess = false;

        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            if (passwordIndex >= 0) { // Check if the index is valid
                String storedPassword = cursor.getString(passwordIndex);
                loginSuccess = storedPassword.equals(hashedPassword);
            }
            cursor.close();
        }
        db.close();
        return loginSuccess;
    }

    public void updateUserProfile(String email, String username, String bio, String location, String visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_BIO, bio);
        values.put(COLUMN_USER_LOCATION, location);
        values.put(COLUMN_PROFILE_VISIBILITY, visibility);

        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?", new String[]{email});
        db.close();
    }

    public Cursor getUserDetails(String email) {
        Log.d("DatabaseHelper", "Getting user details for email: " + email);
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USER, null, COLUMN_USER_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public Cursor getUsersNearby(String userLocation, String excludeUserEmail) {
        Log.v(TAG,userLocation);
        Log.v(TAG,excludeUserEmail);
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v(TAG,COLUMN_PROFILE_VISIBILITY);
        String selection = COLUMN_USER_LOCATION + " = ? AND " + COLUMN_USER_EMAIL + " != ? AND " + COLUMN_PROFILE_VISIBILITY + " = ?";
        String[] selectionArgs = {userLocation, excludeUserEmail, "Visible"};
        return db.query(TABLE_USER, new String[]{COLUMN_USER_EMAIL, COLUMN_USER_LOCATION}, selection, selectionArgs, null, null, "RANDOM()", "1");
    }
}