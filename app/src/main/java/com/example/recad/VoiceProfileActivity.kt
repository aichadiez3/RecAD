package com.example.recad

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

private const val TAG = "ProfileRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class VoiceProfileActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var uploadButton: ImageView
    private lateinit var alert: TextView

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var mediaPlayer: MediaPlayer
    private var player: MediaPlayer? = null
    lateinit var mStorage : StorageReference

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_recording)

        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        uploadButton = findViewById(R.id.upload)
        alert = findViewById(R.id.alert)


    }

/*
    private fun createVoiceProfileDoc(){
        alert.text = "Uploading Voice Profile..."

        database.collection("users").document(user.email.toString()).collection("records").document(recordRef).set(
            hashMapOf("record reference" to recordRef,
                "record date" to currentDate)
        ).addOnSuccessListener {
            Toast.makeText(this, "Collection Records created", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to create document for $recordRef", Toast.LENGTH_LONG).show()
        }
    }
    
 */

}