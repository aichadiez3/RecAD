package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signInButton = findViewById(R.id.createAccount)
        signInButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }

    fun changeActivity(view: View){
        /*
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
 }
*/
    }

}