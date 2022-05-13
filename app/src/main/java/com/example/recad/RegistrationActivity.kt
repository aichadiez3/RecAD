package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class RegistrationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var signInButton: Button
    private lateinit var displayCalendar: ImageView

    private var frag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signInButton = findViewById(R.id.registrationButton)
        signInButton.setOnClickListener {

            frag = supportFragmentManager.findFragmentById(R.id.container)
            if (frag == null) {

                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WelcomeFragment())
                    .commit()

            }
        }

        displayCalendar = findViewById(R.id.calendarImage)
        displayCalendar.setOnClickListener{

            var frag = supportFragmentManager.findFragmentById(R.id.container)
            if (frag == null) {

                supportFragmentManager.beginTransaction()
                    .add(R.id.container, CalendarFragment())
                    .commit()

            }



        }

    }

/*
    fun changeActivity(view: View){

        startActivity(Intent(this, LogInActivity::class.java).apply {
            // To pass any data to next activity
            putExtra("extra_1", value1)
            putExtra("extra_2", value2)
            putExtra("extra_3", value3)
        })

        button.setOnClickListener {
            val intent = Intent(this@MainActivity,SecondActivity::class.java);
            var userName = username.text.toString()
            var password = password_field.text.toString()
            intent.putExtra("Username", userName)
            intent.putExtra("Password", password)
            startActivity(intent);
        }
 }*/



}