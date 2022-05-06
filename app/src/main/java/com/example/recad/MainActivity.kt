package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun logInClick(view: View) {
        Toast.makeText(this, "Log in button clicked",
            Toast.LENGTH_SHORT).show()
        Log.i("info", "The user is logging")

        //Button detection
        val intent = Intent(this, LogInController::class.java)
        startActivity(intent)

    }
}