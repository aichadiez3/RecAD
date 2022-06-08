package com.example.recad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var settings: ImageView
    private lateinit var voice: ImageView
    private lateinit var records: ImageView
    private lateinit var logout: ImageView

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

        logout.findViewById<ImageView>(R.id.loginButton)

        logout.setOnClickListener {

            // Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}