package com.example.notepad

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    //-------------SQLite--------------------------
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    //-----------ListView & FAB--------------------
    lateinit var listView: ListView
    lateinit var fab: FloatingActionButton
    //------------Navigation Drawer----------------
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        fab = findViewById(R.id.floatingActionButton3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setTitle("My Notes")

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        fab.setOnClickListener {
            var intent = Intent(this, ManageNotes::class.java)
            intent.putExtra("note_id", -1)
            startActivity(intent)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, ManageNotes::class.java)
            intent.putExtra("note_id", id)
            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this, UserProfile::class.java))
                }
                R.id.callUs -> {
                    Toast.makeText(this, "Call Us at: +91-7001067632", Toast.LENGTH_LONG).show()
                }
                R.id.mailUs -> {
                    Toast.makeText(this, "Mail Us at: \n com.example.notepad@gmail.com", Toast.LENGTH_LONG).show()
                }
                R.id.aboutUs -> {
                    startActivity(Intent(this, aboutUs::class.java))
                }
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        db = SQLiteDB(this)
        tableDB = db.writableDatabase
//        cursor = tableDB.query(
//            "notes", arrayOf("_id", "title", "description"),
//            null, null, null,
//            null, null
//        )
        cursor = tableDB.rawQuery("select * from notes order by _id DESC", null)
        var customListAdapter = SimpleCursorAdapter(
            this,
            R.layout.list_row,
            cursor,
            arrayOf("title", "description"),
            intArrayOf(R.id.idTitle, R.id.idDesc),
            0
        )
        listView.adapter = customListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        tableDB.close()
        cursor.close()
    }
}