package com.example.notepad

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class aboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_subdirectory_arrow_left_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("About Us")
    }
}