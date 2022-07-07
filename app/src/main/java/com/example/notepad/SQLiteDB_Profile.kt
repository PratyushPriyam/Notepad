package com.example.notepad

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDB_Profile(var ctx: Context): SQLiteOpenHelper(ctx, "db_profile", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        if(db != null) {
            db.execSQL("create table profileData(_id integer primary key autoincrement, name text, aboutMe text)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

}