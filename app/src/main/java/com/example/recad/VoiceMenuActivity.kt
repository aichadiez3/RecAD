package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VoiceMenuActivity : AppCompatActivity() {

    private lateinit var profileButton: ImageView
    private lateinit var startButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voicemenu)


        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            startActivity(Intent(this, RecordingActivity::class.java))
        }

        profileButton = findViewById(R.id.profileButton)
        profileButton.setOnClickListener {
            Toast.makeText(this, "Option is unavailable", Toast.LENGTH_LONG).show()
        }


    }

}