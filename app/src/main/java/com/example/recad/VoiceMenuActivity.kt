package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "VoiceProfileError"

class VoiceMenuActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser

    private lateinit var profileButton: ImageView
    private lateinit var startButton: ImageView



    override fun onResume() {
        super.onResume()

        increaseAlpha(startButton)
        increaseAlpha(findViewById<ImageView>(R.id.imageView6))
        decreaseAlpha(profileButton)
        startButton.isEnabled = true
        profileButton.isEnabled = false

        /*

        database.collection("users").document(user.email.toString()).collection("voice profile").get().addOnSuccessListener {
            increaseAlpha(startButton)
            increaseAlpha(findViewById<ImageView>(R.id.imageView6))
            startButton.isEnabled = true

        }.addOnFailureListener {
            profileButton.isEnabled = true
            increaseAlpha(profileButton)
            Log.e(TAG, "Voice profile not found for this user")
        }

         */

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voicemenu)

        profileButton = findViewById(R.id.profileButton)
        startButton = findViewById(R.id.startButton)

        user = FirebaseAuth.getInstance().currentUser!!

                // Check if the voice profile has been created to enable options

        database.collection("users").document(user.email.toString()).get().addOnSuccessListener { doc ->

            if(doc != null){
                //Log.d(TAG, "Document found")

                var verify = doc.get("voice profile verify")
                if(verify == null) {
                    unableStart()
                } else {
                    Toast.makeText(this,"Profile is already created", Toast.LENGTH_SHORT).show()
                    enableStart()
                }

            } else {
                Log.e(TAG, "Error finding document")
            }


        }.addOnFailureListener {
            Log.e(TAG, "Document not found for this user")
            Toast.makeText(this,"Document not found for this user", Toast.LENGTH_SHORT).show()
            finish()
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


    private fun unableStart(){
        profileButton.isEnabled = true
        startButton.isEnabled = false
        increaseAlpha(profileButton)
        decreaseAlpha(startButton)
        decreaseAlpha(findViewById<ImageView>(R.id.imageView6))
    }

    private fun enableStart(){
        decreaseAlpha(profileButton)
        profileButton.isEnabled = false
        increaseAlpha(startButton)
        increaseAlpha(findViewById<ImageView>(R.id.imageView6))
        startButton.isEnabled = true
    }
}