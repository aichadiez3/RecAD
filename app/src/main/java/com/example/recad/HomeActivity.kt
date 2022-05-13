package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var settings: ImageView
    private lateinit var voice: ImageView
    private lateinit var records: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        voice = findViewById(R.id.voiceButton)
        voice.setOnClickListener {
            startActivity(Intent(this, VoiceMenuActivity::class.java))
        }

        settings = findViewById(R.id.settingsButton)
        settings.setOnClickListener {
            //startActivity(Intent(this, VoiceMenuActivity::class.java))
            Toast.makeText(this, "Settings is unavailable", Toast.LENGTH_LONG).show()
        }

        records = findViewById(R.id.recordsButton)
        records.setOnClickListener {
            //startActivity(Intent(this, VoiceMenuActivity::class.java))
            Toast.makeText(this, "Records is unavailable", Toast.LENGTH_LONG).show()
        }

    }
}