package com.example.ale.todolist.androidsqlite;

import android.provider.BaseColumns;

public class Contract {

    public static final String TABLE_TASKS = "tasks";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    Task.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    Task.COLUMN_TASKNAME + TEXT_TYPE + COMMA_SEP +
                    Task.COLUMN_TASKDESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Task.COLUMN_DAY + TEXT_TYPE + COMMA_SEP +
                    Task.COLUMN_MONTH + TEXT_TYPE + COMMA_SEP +
                    Task.COLUM_YEAR + TEXT_TYPE +

                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_TASKS;

    public Contract() {}

    public static abstract class Task implements BaseColumns {

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TASKNAME = "taskname";
        public static final String COLUMN_TASKDESCRIPTION = "taskdescription";
        public static final String COLUMN_DAY = "taskday";
        public static final String COLUMN_MONTH = "taskmonth";
        public static final String COLUM_YEAR = "taskyear";


    }

}
