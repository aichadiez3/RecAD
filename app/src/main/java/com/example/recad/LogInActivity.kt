package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LogInActivity : AppCompatActivity(){

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerAccount: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerAccount = findViewById(R.id.createAccount)

        registerAccount.setOnClickListener{
            startActivity(Intent(this, LogInActivity::class.java))
        }



    }

}