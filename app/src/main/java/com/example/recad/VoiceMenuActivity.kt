package com.example.recad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class VoiceMenuActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()

    private lateinit var profileButton: ImageView
    private lateinit var startButton: ImageView


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voicemenu)


        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            startActivity(Intent(this, RecordingActivity::class.java))
        }

        profileButton = findViewById(R.id.profileButton)
        profileButton.setOnClickListener {
            //Toast.makeText(this, "Option is unavailable", Toast.LENGTH_LONG).show()

            // SAVE INTO FIREBASE
            val sdf = SimpleDateFormat("dd-M-yyyy-hhmmss")
            val currentDate = sdf.format(Date())
            var record = "record-$currentDate.3gp"
            var creationdate = currentDate

            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                database.collection("users").document(user.email.toString()).collection("records").document(record).set(
                    hashMapOf("record number" to record,
                        "record date" to creationdate)
                )

                Toast.makeText(this, "Collection Records created", Toast.LENGTH_LONG).show()

            }

        }


    }

}