//RecipeDatabaseHelper.java

package com.example.recipeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "recipeDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_NUTRIENTS = "nutrients";

    // Common column names
    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_INGREDIENT_ID = "ingredientId";

    // RECIPES Table - column names
    private static final String KEY_TITLE = "title";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_READY_IN_MINUTES = "readyInMinutes";
    private static final String KEY_SUMMARY = "summary";

    // INGREDIENTS Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_RECIPE_ID_FK = "recipeId";

    // NUTRIENTS Table - column names
    private static final String KEY_NUTRIENT_NAME = "nutrientName";
    private static final String KEY_NUTRIENT_AMOUNT = "nutrientAmount";
    private static final String KEY_NUTRIENT_UNIT = "nutrientUnit";
    private static final String KEY_NUTRIENT_PERCENT_DAILY_NEEDS = "percentDailyNeeds";

    // Table Create Statements
    // Recipe table create statement
    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE "
            + TABLE_RECIPES + "("
            + KEY_RECIPE_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_SERVINGS + " INTEGER,"
            + KEY_IMAGE + " TEXT,"
            + KEY_READY_IN_MINUTES + " INTEGER,"
            + KEY_SUMMARY + " TEXT" + ")";

    // Ingredient table create statement
    private static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE "
            + TABLE_INGREDIENTS + "("
            + KEY_INGREDIENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_AMOUNT + " REAL,"
            + KEY_UNIT + " TEXT,"
            + KEY_RECIPE_ID_FK + " INTEGER,"
            + "FOREIGN KEY(" + KEY_RECIPE_ID_FK + ") REFERENCES " + TABLE_RECIPES + "(" + KEY_RECIPE_ID + ")" + ")";

    // Nutrient table create statement
    private static final String CREATE_TABLE_NUTRIENTS = "CREATE TABLE "
            + TABLE_NUTRIENTS + "("
            + KEY_NUTRIENT_NAME + " TEXT,"
            + KEY_NUTRIENT_AMOUNT + " REAL,"
            + KEY_NUTRIENT_UNIT + " TEXT,"
            + KEY_NUTRIENT_PERCENT_DAILY_NEEDS + " REAL,"
            + KEY_RECIPE_ID_FK + " INTEGER,"
            + "FOREIGN KEY(" + KEY_RECIPE_ID_FK + ") REFERENCES " + TABLE_RECIPES + "(" + KEY_RECIPE_ID + ")" + ")";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_NUTRIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUTRIENTS);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUTRIENTS);
        onCreate(db);
    }

    public void addRecipeAndIngredients(long recipeId, String title, int servings, String image, int readyInMinutes, String summary, JSONArray ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert into the RECIPES table
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(KEY_RECIPE_ID, recipeId);
        recipeValues.put(KEY_TITLE, title);
        recipeValues.put(KEY_SERVINGS, servings);
        recipeValues.put(KEY_IMAGE, image);
        recipeValues.put(KEY_READY_IN_MINUTES, readyInMinutes);
        recipeValues.put(KEY_SUMMARY, summary);

        // Insert the recipe
        db.insert(TABLE_RECIPES, null, recipeValues);

        // Insert into the INGREDIENTS table
        try {
            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredientObject = ingredients.getJSONObject(i);
                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put(KEY_NAME, ingredientObject.getString("name"));
                ingredientValues.put(KEY_AMOUNT, ingredientObject.getDouble("amount"));
                ingredientValues.put(KEY_UNIT, ingredientObject.getString("unit"));
                ingredientValues.put(KEY_RECIPE_ID_FK, recipeId);

                // Insert the ingredient
                db.insert(TABLE_INGREDIENTS, null, ingredientValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Close the database connection
        db.close();
    }
}
