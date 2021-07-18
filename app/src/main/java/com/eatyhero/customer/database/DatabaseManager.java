package com.eatyhero.customer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eatyhero.customer.model.CartDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager
{
    private static Database database;
    public static DatabaseManager databaseManager=null;
    SQLiteDatabase db;
    Context context;
    Cursor c;

    public static DatabaseManager getInstance(Context context)
    {
        if(databaseManager ==null)
        {
            databaseManager = new DatabaseManager(context);
        }
        return  databaseManager;
    }

    public DatabaseManager(Context context)
    {
        super();
        this.context = context;
        database = new Database(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
    }


    public void openDatabase()
    {
        db = database.getWritableDatabase();
    }

    public void closeDatabase()
    {
        database.close();
    }


    // insert into table
    public void insert(CartDetails cartItems)
    {
        ContentValues cv = new ContentValues();
        cv.put(Database.MENU_ID, cartItems.getMenuId());
        cv.put(Database.RESTAURANT_ID, cartItems.getResId());
        cv.put(Database.MENU_NAME, cartItems.getMenuName());
        cv.put(Database.MENU_TYPE, cartItems.getMenuType());
        cv.put(Database.MENU_SIZE, cartItems.getMenuSize());
        cv.put(Database.MENU_PRICE, cartItems.getMenuPrice());
        cv.put(Database.ADDON_NAME, cartItems.getAddonName());
        cv.put(Database.ADDON_PRICE, cartItems.getAddonPrice());
        cv.put(Database.QUANTITY, cartItems.getQuantity());
        cv.put(Database.TOTAL_PRICE, cartItems.getTotalPrice());
        cv.put(Database.INSTRUCTION, cartItems.getInstruction());

        Log.e("cv","cv"+cv);

        Log.e("MAINMmenuId",cartItems.getMenuId());

/*
        try
        {
            db.insert(Database.DATABASE_TABLE, null, cv);
            Log.e("Table ", "Values inserted");
            db.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Log.e("Insert problem", "");
        }*/

        //Menu Quantity Update method
        String rowID = null;
        String totalQuantity=null;
        String oldQuantity=null;

        if (getCount() > 0)
        {
            //Check Same menu and same addons in table
            Cursor c = db.rawQuery("SELECT * FROM " + Database.DATABASE_TABLE + " WHERE " +
                    Database.MENU_ID + "=? AND " + Database.MENU_SIZE + "=? AND " + Database.ADDON_NAME + "=? AND " +Database.MENU_NAME +"=?", new String[]{cartItems.getMenuId(), cartItems.getMenuSize(), cartItems.getAddonName(), cartItems.getMenuName()});

            if (c.moveToFirst())
            {
                rowID = c.getString(0);
                oldQuantity = c.getString(9);

                int Totalquantity = Integer.valueOf(oldQuantity) + Integer.valueOf(cartItems.getQuantity());

                Log.e("Current Total", "" + Totalquantity);

                totalQuantity = String.valueOf(Totalquantity);

                //call update quantity method for update new quantity
                updateQuantity(rowID, totalQuantity);
            }
            else
            {
                try
                {
                    db.insert(Database.DATABASE_TABLE, null, cv);
                    Log.e("cv","cv"+cv);
                    Log.e("Table ", "Values inserted");
                    db.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    Log.e("Insert problem", "");
                }
            }

        }
        else
        {
            try
            {
                db.insert(Database.DATABASE_TABLE, null, cv);
                Log.e("cv","cv"+cv);
                db.close();
                Log.e("Inserted ", "Values inserted");
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                Log.e("Insert problem", "");
            }
        }
    }

    public Cursor getAll()
    {
        Cursor cursor = null;
        String query = "select * from " + Database.DATABASE_TABLE;
        db = database.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        return cursor;
    }

    //get all using curser
    public Cursor getIDValues(String username)
    {
        SQLiteDatabase DB = database.getReadableDatabase();
        String query = "select * from " + Database.DATABASE_TABLE + " where username= " + "'" + username + "'";

        c = DB.rawQuery(query, null);

        return c;
    }

    //get count
    public int getCount()
    {
        String query = "select * from " + Database.DATABASE_TABLE;
        db = database.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        return c.getCount();
    }

    //get count
    public int getQuantityCount()
    {
        int qty=0;
        String query = "select * from " + Database.DATABASE_TABLE;
        db = database.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
        {
            do {

                int Qty =Integer.parseInt(c.getString(9));

                qty += Qty;

            } while (c.moveToNext());
        }

        c.close();

        closeDatabase();


        return qty;
    }


    //get SubTotal
    public String getSubTotal()
    {
        double SUB_TOTAL=0;

        Cursor cursor = null;

        String query = "select * from " + Database.DATABASE_TABLE;

        openDatabase();

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {

                double sub_TOTAL =Double.parseDouble(cursor.getString(10));

                SUB_TOTAL += sub_TOTAL;

            } while (cursor.moveToNext());
        }

        cursor.close();

        closeDatabase();

        return String.format("%.2f", SUB_TOTAL);
    }


    //Clear table
    public void clearTable()
    {
        db = database.getWritableDatabase();
        db.delete(Database.DATABASE_TABLE, null, null);
    }

    //update quantity
    public void updateQuantity(String id, String quantity)
    {
        db = database.getWritableDatabase();

        int match = Integer.parseInt(quantity);

        if (match == 0)
        {
            deleteMenu(id);
        }
        else if (match > 0)
        {
            ContentValues cv = new ContentValues();
            cv.put(Database.QUANTITY, quantity);
            db.update(Database.DATABASE_TABLE, cv, Database.PRIMARY_KEY + "=" + id, null);

            //call update method to update total price
            updatePrice(id);
        }

    }

    //Delete Menu Items
    public void deleteMenu(String id)
    {
        Log.d("Items", "Item Deleted" + id);

        db = database.getWritableDatabase();
        db.delete(Database.DATABASE_TABLE, Database.PRIMARY_KEY + "=" + id, null);
        db.close();

    }

    //get quantity and menu price and addon price to update
    public void updatePrice(String id)
    {
        String menuPrice=null;
        String addonPrice=null;
        String quantity=null;

        SQLiteDatabase DB = database.getReadableDatabase();

        String query = "select * from " + Database.DATABASE_TABLE + " where id= " + "'" + id + "'";

        Cursor cursor = DB.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
           menuPrice = cursor.getString(6);
           addonPrice = cursor.getString(8);
           quantity = cursor.getString(9);
        }

        double menu_Price = Double.parseDouble(menuPrice);
        double addon_Price = Double.parseDouble(addonPrice);
        double qty = Integer.parseInt(quantity);

        //Add Total price
        double total = (menu_Price + addon_Price) * qty;

        db = database.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Database.TOTAL_PRICE, String.valueOf(total));
        db.update(Database.DATABASE_TABLE, cv, Database.PRIMARY_KEY + "=" + id, null);
        db.close();
    }


    public JSONArray getCart()
    {
        JSONArray jsonArray = new JSONArray();

        Cursor cursor = null;

        String query = "select * from " + Database.DATABASE_TABLE;

        db = database.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("menu_id", cursor.getString(1));
                    jsonObject.put("res_id", cursor.getString(2));
                    if(cursor.getString(5).isEmpty()){
                        jsonObject.put("menu_name", cursor.getString(3));
                    }else{
                        jsonObject.put("menu_name", cursor.getString(3)+" ["+cursor.getString(5)+"] ");
                    }

                    jsonObject.put("menu_type", cursor.getString(4));
                    jsonObject.put("menu_size", cursor.getString(5));
                    jsonObject.put("Menu_price", cursor.getString(6));
                    jsonObject.put("Addon_name", cursor.getString(7));
                    jsonObject.put("Addon_price", cursor.getString(8));
                    jsonObject.put("quantity", cursor.getString(9));
                    jsonObject.put("Total", cursor.getString(10));
                    jsonObject.put("menu_instruction", cursor.getString(11));

                    jsonArray.put(jsonObject);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();

        Log.w("Cart details", "Cart details : " + jsonArray);

        return jsonArray;

    }

    public String getResId()
    {

        String resId = "";

        Cursor cursor = null;

        String query = "select * from " + Database.DATABASE_TABLE;

        db = database.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                try
                {


                    resId = cursor.getString(2);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();



        return resId;

    }

    public String getQuantity(String menuId){


        Log.e("menuId",menuId);

        String oldQuantity = null;

        Cursor c = db.rawQuery("SELECT * FROM " + Database.DATABASE_TABLE + " WHERE " +
                Database.MENU_ID + "=?", new String[]{menuId});

        if (c.moveToFirst()) {

            oldQuantity = c.getString(9);
        }

        return oldQuantity;
    }

}
