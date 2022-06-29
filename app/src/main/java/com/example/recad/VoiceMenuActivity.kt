package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class VoiceMenuActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser

    private lateinit var profileButton: ImageView
    private lateinit var startButton: ImageView


    override fun onResume() {
        super.onResume()
        database.collection("users").document(user.email.toString()).collection("voice profile").get().addOnSuccessListener {

            increaseAlpha(startButton)
            increaseAlpha(findViewById<ImageView>(R.id.imageView6))
            startButton.isEnabled = true

        }.addOnFailureListener {
            profileButton.isEnabled = true
            increaseAlpha(profileButton)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voicemenu)

        profileButton = findViewById(R.id.profileButton)
        startButton = findViewById(R.id.startButton)

        startButton.isEnabled = false
        profileButton.isEnabled = false

        user = FirebaseAuth.getInstance().currentUser!!

    // Check if the voice profile has been created to enable options

        database.collection("users").document(user.email.toString()).collection("voice profile").get().addOnSuccessListener {
            Toast.makeText(this,"Profile is already created", Toast.LENGTH_SHORT).show()
            increaseAlpha(startButton)
            increaseAlpha(findViewById<ImageView>(R.id.imageView6))
            startButton.isEnabled = true

        }.addOnFailureListener {
            profileButton.isEnabled = true
            increaseAlpha(profileButton)
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, VoiceProfileActivity::class.java))
        }


        startButton.setOnClickListener {
            startActivity(Intent(this, RecordingActivity::class.java))
        }








    }

    private fun decreaseAlpha(v: View){
        v.alpha = 0.5F
    }
    private fun increaseAlpha(v: View){
        v.alpha = 1F
    }

}