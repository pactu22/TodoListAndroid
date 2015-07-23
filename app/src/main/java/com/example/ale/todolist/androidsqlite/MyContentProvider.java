package com.example.ale.todolist.androidsqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class MyContentProvider extends ContentProvider {

    private DBHelper myDB;
    private SQLiteDatabase database;


    private static final String AUTHORITY = "com.example.ale.todolist.androidsqlite.MyContentProvider";
    public static final String TASKS_TABLE = Contract.TABLE_TASKS;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TASKS_TABLE);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int TASKS = 1;
    public static final int TASKS_ID = 2;

    static {
        sURIMatcher.addURI(AUTHORITY, TASKS_TABLE, TASKS);
        sURIMatcher.addURI(AUTHORITY, TASKS_TABLE + "/#", TASKS_ID);
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHelper(getContext(), null, null, 1);
        /* With these lines, the db will be created every time the app starts, so the values stored will disappear
        database = myDB.getWritableDatabase();
        if(database == null) return false;
        else return true; */
        return false;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Contract.TABLE_TASKS);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case TASKS_ID:
                queryBuilder.appendWhere(Contract.Task.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case TASKS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        //register to watch a content URI for changes

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case TASKS:
                id = sqlDB.insert(Contract.TABLE_TASKS, null, values);
                if(id > 0) {
                    Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TASKS_TABLE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case TASKS:
                rowsDeleted = sqlDB.delete(Contract.TABLE_TASKS,
                        selection,
                        selectionArgs);
                break;

            case TASKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Contract.TABLE_TASKS,
                            Contract.Task.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(Contract.TABLE_TASKS,
                            Contract.Task.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case TASKS:
                rowsUpdated = sqlDB.update(Contract.TABLE_TASKS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(Contract.TABLE_TASKS,
                                    values,
                                    Contract.Task.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(Contract.TABLE_TASKS,
                                    values,
                                    Contract.Task.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
