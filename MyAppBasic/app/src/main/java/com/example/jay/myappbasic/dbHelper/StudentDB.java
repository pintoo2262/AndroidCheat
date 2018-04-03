package com.example.jay.myappbasic.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jay.myappbasic.model.Student;

import java.util.ArrayList;

/**
 * Created by jay on 19-03-2018.
 */

public class StudentDB {
    DatabaseHandler mDatabaseHandler;
    SQLiteDatabase mSqLiteDatabase;

    public StudentDB(Context mContext) {
        mDatabaseHandler = new DatabaseHandler(mContext);
    }

    public void Add_Student(Student model) {
        mSqLiteDatabase = mDatabaseHandler.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DButils.Student_Id, model.getRollNo());
        value.put(DButils.Student_Name, model.getName());
        value.put(DButils.Student_CollegeName, model.getCollegeName());
        mSqLiteDatabase.insert(DButils.StudentTable, null, value);
        mSqLiteDatabase.close();
    }

    public void update_Student(Student model, int rollNo) {
        mSqLiteDatabase = mDatabaseHandler.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DButils.Student_Id, model.getRollNo());
        value.put(DButils.Student_Name, model.getName());
        value.put(DButils.Student_CollegeName, model.getCollegeName());
        mSqLiteDatabase.update(DButils.StudentTable, value, "roll_No = ?", new String[]{String.valueOf(rollNo)});
        mSqLiteDatabase.close();
    }

    public void delete_Student(int rollNo) {
        mSqLiteDatabase = mDatabaseHandler.getWritableDatabase();
        mSqLiteDatabase.delete(DButils.StudentTable, "roll_No = ?", new String[]{String.valueOf(rollNo)});
        mSqLiteDatabase.close();
    }


    public ArrayList<Student> get_All_Student() {
        ArrayList<Student> Arr = null;
        mSqLiteDatabase = mDatabaseHandler.getReadableDatabase();
        Cursor c = mSqLiteDatabase.query(DButils.StudentTable, null, null,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<Student>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Student data = new Student();
                data.setRollNo(c.getInt(0));
                data.setName(c.getString(1));
                data.setCollegeName(c.getString(2));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            mSqLiteDatabase.close();
            return Arr;
        }
        return Arr;

    }

    public ArrayList<Student> get_single_student(int roll_No) {
        ArrayList<Student> Arr = null;
        mSqLiteDatabase = mDatabaseHandler.getReadableDatabase();
        Cursor c = mSqLiteDatabase.query(DButils.StudentTable, null, "roll_No=" + roll_No,
                null, null, null, null);
        if (c.getCount() > 0) {
            Arr = new ArrayList<Student>();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Student data = new Student();
                data.setRollNo(c.getInt(0));
                data.setName(c.getString(1));
                data.setCollegeName(c.getString(2));
                Arr.add(data);
                c.moveToNext();
            }
            c.close();
            mSqLiteDatabase.close();
            return Arr;

        }

        return Arr;

    }






}
