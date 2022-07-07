package com.example.notepad

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ManageNotes : AppCompatActivity() {
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var contentValue: ContentValues
    var note_id: Long = 0
    lateinit var txtTitle: EditText
    lateinit var txtDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_notes)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_subdirectory_arrow_left_24)
        supportActionBar?.setTitle("Manage My Notes")
        note_id = intent.getLongExtra("note_id", -1)
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDesc)
        db = SQLiteDB(this)
        tableDB = db.writableDatabase
        contentValue = ContentValues()

        if(note_id > 0) {
            tableDB = db.readableDatabase
            cursor = tableDB.query("notes", arrayOf("title", "description"), "_id=?", arrayOf(note_id.toString()), null, null, null)
//            cursor = tableDB.rawQuery("select * from notes ", null)
            if(cursor.moveToFirst()) {
                txtTitle.setText(cursor.getString(0))
                txtDescription.setText(cursor.getString(1))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       menuInflater.inflate(R.menu.save_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save -> {
                saveNote() // Save
            }
            R.id.delete -> {
                deleteNote()
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return true
    }

    private fun deleteNote() {
        tableDB.delete("notes", "_id=?", arrayOf(note_id.toString()))
        txtTitle.setText("")
        txtDescription.setText("")
        txtTitle.requestFocus()
        Toast.makeText(this, "Deletion Successful", Toast.LENGTH_SHORT).show()
    }

    private fun saveNote() {
        if(note_id.toInt() == -1) { // New Save
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDescription.text.toString())
            tableDB.insert("notes", null, contentValue)
            txtTitle.setText("")
            txtDescription.setText("")
            txtTitle.requestFocus()
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
        }
        if(note_id > 0) { // Update
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDescription.text.toString())
            tableDB.update("notes", contentValue, "_id=?", arrayOf(note_id.toString()))
            txtTitle.setText("")
            txtDescription.setText("")
            txtTitle.requestFocus()
            Toast.makeText(this, "Updation Successful", Toast.LENGTH_SHORT).show()
            contentValue.clear()
        }
    }
}