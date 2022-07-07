package com.example.notepad

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class UserProfile : AppCompatActivity() {
    lateinit var db: SQLiteDB_Profile
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var contentValue: ContentValues
    lateinit var button: Button
    lateinit var idTit: TextView
    lateinit var idDesc: TextView
    var id = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_subdirectory_arrow_left_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("My Profile")
        button = findViewById(R.id.button)
        idTit = findViewById(R.id.idTit)
        idDesc = findViewById(R.id.idDesc)
        db = SQLiteDB_Profile(this)
        tableDB = db.writableDatabase
        contentValue = ContentValues()

        button.setOnClickListener {
            var intent = Intent(this, EnterProfileData::class.java)
            intent.putExtra("name", idTit.text.toString())
            intent.putExtra("aboutMe", idDesc.text.toString())
            startActivity(intent)
        }

        val editName: String = intent.getStringExtra("editName").toString()
        val editAboutMe: String = intent.getStringExtra("editAboutMe").toString()
        idTit.setText(editName)
        idDesc.setText(editAboutMe)


        cursor = tableDB.rawQuery("SELECT * FROM profileData ORDER BY _id DESC LIMIT 1", null)
        if(cursor.moveToFirst()) {
            idTit.setText(cursor.getString(1))
            idDesc.setText(cursor.getString(2))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}