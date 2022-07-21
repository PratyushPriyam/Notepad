package com.example.notepad

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

class ManageNotes : AppCompatActivity() {
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var contentValue: ContentValues
    var note_id: Long = 0
    lateinit var txtTitle: EditText
    lateinit var txtDescription: EditText

    lateinit var speakTitle: Button
    lateinit var speakDescription: Button
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var replacedEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_notes)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_subdirectory_arrow_left_24)
        supportActionBar?.setTitle("Manage My Notes")
        note_id = intent.getLongExtra("note_id", -1)
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDesc)
        db = SQLiteDB(this)
        tableDB = db.writableDatabase
        contentValue = ContentValues()
        speakTitle = findViewById(R.id.speakTitle)
        speakDescription = findViewById(R.id.speakDescription)

        speakTitle.setOnClickListener {
            speechToText()
            replacedEditText = findViewById(R.id.txtTitle)
        }

        speakDescription.setOnClickListener {
            speechToText()
            replacedEditText = findViewById(R.id.txtDesc)
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if(result?.resultCode == RESULT_OK && result?.data != null) {
                val speechText = result?.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                replacedEditText.text = speechText[0]
            }
        }

        if(note_id > 0) {
            tableDB = db.readableDatabase
            cursor = tableDB.query("notes", arrayOf("title", "description"), "_id=?", arrayOf(note_id.toString()), null, null, null)
            if(cursor.moveToFirst()) {
                txtTitle.setText(cursor.getString(0))
                txtDescription.setText(cursor.getString(1))
            }
        }


    }

    private fun speechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now !!")
        try {
            activityResultLauncher.launch(intent)
        }
        catch (exception: ActivityNotFoundException) {
            Toast.makeText(this, "Speech Recognition not available", Toast.LENGTH_SHORT).show()
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
//        txtTitle.setText("")
//        txtDescription.setText("")
//        txtTitle.requestFocus()
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Deletion Successful", Toast.LENGTH_SHORT).show()
    }

    private fun saveNote() {
        if(note_id.toInt() == -1) { // New Save
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDescription.text.toString())
            tableDB.insert("notes", null, contentValue)
//            txtTitle.setText("")
//            txtDescription.setText("")
//            txtTitle.requestFocus()
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
        }
        if(note_id > 0) { // Update
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDescription.text.toString())
            tableDB.update("notes", contentValue, "_id=?", arrayOf(note_id.toString()))
//            txtTitle.setText("")
//            txtDescription.setText("")
//            txtTitle.requestFocus()
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Updation Successful", Toast.LENGTH_SHORT).show()
            contentValue.clear()
        }
    }
}