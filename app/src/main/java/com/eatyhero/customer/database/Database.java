package com.eatyhero.customer.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "foodordering";
    public static final String DATABASE_TABLE = "Cart";
    public static final String PRIMARY_KEY = "id";
    public static final String MENU_ID = "menuid";
    public static final String RESTAURANT_ID = "resid";
    public static final String MENU_NAME = "menuName";
    public static final String MENU_TYPE = "menuType";
    public static final String MENU_SIZE = "menuSize";
    public static final String QUANTITY = "quantity";
    public static final String ADDON_NAME = "addonName";
    public static final String ADDON_PRICE = "addonPrice";
    public static final String MENU_PRICE = "menuPrice";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String INSTRUCTION = "menu_instruction";
    public static final int DATABASE_VERSION = 2;

    String CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE +
            "(" + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MENU_ID + " TEXT NOT NULL,"
            + RESTAURANT_ID + " TEXT NOT NULL,"
            + MENU_NAME + " TEXT NOT NULL,"
            + MENU_TYPE + " TEXT NOT NULL,"
            + MENU_SIZE + " TEXT,"
            + MENU_PRICE + " TEXT NOT NULL,"
            + ADDON_NAME + " TEXT,"
            + ADDON_PRICE + " TEXT,"
            + QUANTITY + " TEXT NOT NULL,"
            + TOTAL_PRICE + " TEXT NOT NULL,"
            + INSTRUCTION + " TEXT" + ")";


    public Database(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        Log.d("DB Created ", "Database created success ");
    }

    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(CREATE_TABLE);
            Log.e("Table Created", "Table created successfully");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Log.e("Table Error", "" + e.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

}
