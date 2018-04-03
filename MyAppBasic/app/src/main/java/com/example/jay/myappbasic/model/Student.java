package com.example.jay.myappbasic.model;

/**
 * Created by jay on 18-03-2018.
 */

public class Student {
    int rollNo;
    String name;
    String collegeName;

    public int getRollNo() {
        return rollNo;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getName() {
        return name;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
