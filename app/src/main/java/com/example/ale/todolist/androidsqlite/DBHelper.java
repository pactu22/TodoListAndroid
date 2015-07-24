package com.example.ale.todolist.androidsqlite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskDB.db";

    private ContentResolver myCR;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("CREATING", "dassssd");
        db.execSQL(Contract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("UPDATIGGGGGGGGGGGGGGGG", "dasd");
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TABLE_TASKS);
    }

    /* public void addProduct(String id, String name, String qqt) {

         ContentValues values = new ContentValues();
         values.put(Contract.Product.COLUMN_PRODUCTNAME, name);
         values.put(Contract.Product.COLUMN_QUANTITY, qqt);

         myCR.insert(MyContentProvider.CONTENT_URI, values);
     }

     public Cursor findProduct(String productname) {
         String[] projection = {Contract.Product.COLUMN_ID, Contract.Product.COLUMN_PRODUCTNAME, Contract.Product.COLUMN_QUANTITY };

         String selection = "productname = \"" + productname + "\"";

         Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                 projection, selection, null,
                 null);

         return cursor;
     }
 */
    public int deleteTask(String idTask) {
        /*
        String query = "Delete FROM " + Contract.TABLE_TASKS + " WHERE " + Contract.Task.COLUMN_ID + " =  \"" + idTask + "\"";

        Log.d("error", query);
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);
        */
        String where = "_id = \"" + idTask + "\"";

        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI, where, null);
        return rowsDeleted;
    }

    public void addTask(String name, String description, String day, String month, String year) {

        ContentValues values = new ContentValues();
        values.put(Contract.Task.COLUMN_TASKNAME, name);
        values.put(Contract.Task.COLUMN_TASKDESCRIPTION, description);
        values.put(Contract.Task.COLUMN_DAY, day);
        values.put(Contract.Task.COLUMN_MONTH, month);
        values.put(Contract.Task.COLUMN_YEAR, year);

        Uri uri = myCR.insert(MyContentProvider.CONTENT_URI, values);
        Log.d("JAVAAAAAAAAa", uri.toString());
        //SQLiteDatabase db = this.getWritableDatabase();

        //db.insert(Contract.TABLE_TASKS, null, values);
    }

    public void updateTask(String id, String name, String description, String day, String month, String year) {


        ContentValues values = new ContentValues();
        values.put(Contract.Task.COLUMN_TASKNAME, name);
        values.put(Contract.Task.COLUMN_TASKDESCRIPTION, description);
        values.put(Contract.Task.COLUMN_DAY, day);
        values.put(Contract.Task.COLUMN_MONTH, month);
        values.put(Contract.Task.COLUMN_YEAR, year);
/*
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(Contract.TABLE_TASKS, values, Contract.Task._ID +" = "+id, null);*/

        String selection = "_id = \"" + id + "\"";

        int cursor = myCR.update(MyContentProvider.CONTENT_URI,
                values, selection, null);
        Log.d("rows modified", String.valueOf(cursor));

    }


    public Cursor findTaskById(String id) {
        /*
        String query = "Select * FROM " + Contract.TABLE_TASKS + " WHERE " + Contract.Task.COLUMN_ID + " =  \"" + id + "\"";
        Log.d("error", query);
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
*/

       // String[] projection = {Contract.Task.COLUMN_ID, Contract.Product.COLUMN_PRODUCTNAME, Contract.Product.COLUMN_QUANTITY };

        String selection = "_id = \"" + id + "\"";

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                null, selection, null,
                null);

        return cursor;

        //http://www.mkyong.com/android/android-date-picker-example/
    }

    public Cursor allTasks(){
        /*
        String query = "Select * FROM " + Contract.TABLE_TASKS + " ORDER BY "+
                Contract.Task.COLUMN_DAY
                ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);*/
        //http://www.mkyong.com/android/android-date-picker-example/

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI, null, null, null, null);
        //String result = "Javacodegeeks Results:";

        return cursor;
    }
}
