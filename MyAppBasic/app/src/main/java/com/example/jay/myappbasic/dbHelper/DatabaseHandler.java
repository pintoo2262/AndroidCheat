package com.example.jay.myappbasic.dbHelper;

/**
 * Created by jay on 18-03-2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {



    String CREATE_STUDENT_TABLE = "CREATE TABLE " + DButils.StudentTable + "("
            + DButils.Student_Id + " INTEGER PRIMARY KEY," + DButils.Student_Name + " TEXT,"
            + DButils.Student_CollegeName + " TEXT" + ")";

    public DatabaseHandler(Context context) {
        super(context, DButils.DATABASE_NAME, null, DButils.DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DButils.StudentTable);

        // Create tables again
        onCreate(db);
    }
}
