package com.example.projectdb;

import static android.os.Build.ID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";

    public dbHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + CUSTOMER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT," +
                COLUMN_CUSTOMER_AGE + " INTEGER," +
                COLUMN_ACTIVE_CUSTOMER + " BOOLEAN)";
        db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean updateCustomer(int id, String name, int age, boolean isActive) {

            SQLiteDatabase dbs = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CUSTOMER_NAME, name);
            cv.put(COLUMN_CUSTOMER_AGE, age);
            cv.put(COLUMN_ACTIVE_CUSTOMER, isActive);

            long update = dbs.update(CUSTOMER_TABLE, cv, "id=?", new String[]{String.valueOf(id)});

            if (update == -1) {
                return  false;
            }
            else {
                return  true;
            }


    }

    public boolean addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER_NAME, customer.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customer.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER,customer.isActive());

        long insert =  db.insert(CUSTOMER_TABLE,null,cv);

        if (insert == -1) {
            return  false;
        }
        else {
            return  true;
        }
    }

    public List<Customer> getAll() {
        List<Customer> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);


        if(cursor.moveToFirst()) {
            do {
                int customerId = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive;
                if (cursor.getInt(3)==1) {
                    customerActive = true;
                }
                else {
                    customerActive = false;
                }

                Customer customer = new Customer(customerId,customerName,customerAge,customerActive);
                returnList.add(customer);

            }while (cursor.moveToNext());

        }
        else {
            // Do nothing;
        }

        cursor.close();
        db.close();
        return returnList;

    }

    public boolean deleteCustomer(Customer customer){
        // find the cx if present if found -> delete and return true

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID +" = "+customer.getId();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }

    }
}
