package com.example.notepad

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EnterProfileData : AppCompatActivity() {
    lateinit var db: SQLiteDB_Profile
    lateinit var tableDB: SQLiteDatabase
    lateinit var contentValue: ContentValues
    lateinit var text1: EditText
    lateinit var text2: EditText
    lateinit var submit_button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_profile_data)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_subdirectory_arrow_left_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Edit Your Bio")

        db = SQLiteDB_Profile(this)
        tableDB = db.writableDatabase
        contentValue = ContentValues()
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)
        submit_button = findViewById(R.id.submit_button)

        val name = intent.getStringExtra("name")
        val aboutMe = intent.getStringExtra("aboutMe")
        text1.setText(name)
        text2.setText(aboutMe)

        submit_button.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            intent.putExtra("editName", text1.text.toString())
            intent.putExtra("editAboutMe", text2.text.toString())
            contentValue.put("name", text1.text.toString())
            contentValue.put("aboutMe", text2.text.toString())
            tableDB.insert("profileData", null, contentValue)
            Toast.makeText(this, "Data Changed", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}