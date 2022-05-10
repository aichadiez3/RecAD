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
        /*
        startActivity(Intent(this, LogInActivity::class.java).apply {
            // To pass any data to next activity
            putExtra("extra_1", value1)
            putExtra("extra_2", value2)
            putExtra("extra_3", value3)
        })
*/
    }

}