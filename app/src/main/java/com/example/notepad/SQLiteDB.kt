package com.example.notepad

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDB(var ctx: Context): SQLiteOpenHelper(ctx, "Notepad_Database", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        if(db != null) {
            db.execSQL("create table notes(_id integer primary key autoincrement, title text, description text)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}