package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    fun changeActivity(view: View){
        val intent = Intent(this, RegistrationActivity::class.java)
        // To pass any data to next activity
        //intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }

}