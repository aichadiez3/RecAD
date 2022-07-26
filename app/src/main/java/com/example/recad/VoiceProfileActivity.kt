package com.example.recad

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

private const val TAG = "ProfileRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 201

class VoiceProfileActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var uploadButton: ImageView
    private lateinit var alert: TextView
    private lateinit var alert2: TextView

    private lateinit var mediaRecorder: MediaRecorder
    lateinit var mStorage : StorageReference

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)


    //Private variables to be used
    private var filePath: String = ""
    private var count: Int = 1
    private var isRecording: Boolean = false

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        uploadButton = findViewById(R.id.upload)
        alert = findViewById(R.id.alert)
        alert2 = findViewById(R.id.alert2)

        user = FirebaseAuth.getInstance().currentUser!!
        mStorage = FirebaseStorage.getInstance().getReference("Voice Profiles")

        try{
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            enableStart()
        } catch(e: Exception){
            alert.text = "Permissions denied"
            Log.e(TAG, "permissions failed exception: ", e)
            finish()
        }


        startButton.setOnClickListener {

            alert2.isVisible = true
            alert2.text = "Record: $count/3"
            if(count>1){
                findViewById<ImageView>(R.id.done).isVisible = false
            }

            filePath = Environment.getExternalStorageDirectory().absolutePath
            filePath += "/${user.uid}_$count.3gp"
            startRecording(filePath)
        }

        stopButton.setOnClickListener {
            Toast.makeText(this, "Stop pressed. Waiting to upload...", Toast.LENGTH_LONG).show()
            stopRecording()
            findViewById<ImageView>(R.id.done).isVisible = false
            findViewById<ImageView>(R.id.sync).isVisible = true
            uploadAudio("${user.uid}" + "_" + "$count")
            createVoiceProfileDoc(count)

            if(count == 3) {
                waitBeforeClose()
            } else {
                count++
            }


        }



    }


    private fun createVoiceProfileDoc(count: Int){
        alert.text = "Uploading Voice Profile..."
        val randomNum = (10000000..19999999).shuffled().last()
        findViewById<ImageView>(R.id.sync).isVisible = false
        findViewById<ImageView>(R.id.done).isVisible = true

        database.collection("users").document(user.email.toString()).collection("voice profile").document("profile $count").set(
            hashMapOf("voice reference" to randomNum)
        ).addOnSuccessListener {
            Toast.makeText(this, "Collection Voice Profile created", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Log.e(TAG,"Failed to create document for ${user.uid}")
        }

    }

    private fun uploadAudio(reference: String){
        alert.text = "Uploading audio..."
        findViewById<ImageView>(R.id.sync).isVisible = true

        var path = mStorage.child(reference)
        var uri = Uri.fromFile(File(filePath))

        path.putFile(uri).addOnSuccessListener {
            alert.text = "Profile Uploaded"
            findViewById<ImageView>(R.id.sync).isVisible = false
            findViewById<ImageView>(R.id.done).isVisible = true
            enableStart()
        }
    }

    private fun waitBeforeClose(){

        findViewById<ImageView>(R.id.done).isVisible = true
        alert.text = "Voice Profile Creation Completed"

        database.collection("users").document(user.email.toString()).update("voice profile verify", true).addOnSuccessListener {

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable {
                finish()
            }, 1500)

        }.addOnFailureListener {
                Toast.makeText(this, "Error updating verification", Toast.LENGTH_SHORT).show()
        }




    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            isDisconnected()
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    @Suppress("DEPRECATION")
    private fun startRecording(filePath:String){
        mediaRecorder = MediaRecorder().apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(filePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare()
                isRecording = true

            } catch (e: IOException) {
                Log.e(TAG, "start recording prepare() failed")
                isRecording = false
            }

            start()
            enableStop()

        }
    }

    private fun stopRecording() {
        alert.text = "Recording Finished"
        if(isRecording){
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false
        } else {
            mediaRecorder.release()
        }



    }


    /** BUTTON VISUALISATION FUNCTIONS */


    private fun enableStart(){
        startButton.isVisible = true
        startButton.isEnabled = true
        stopButton.isVisible = false
        stopButton.isEnabled = false
        alert.text = "Press to start recording"
    }
    private fun enableStop(){
        startButton.isVisible = false
        startButton.isEnabled = false
        stopButton.isVisible = true
        stopButton.isEnabled = true
        alert.text = "Recording Started..."
    }


    private fun isDisconnected(){
        findViewById<ImageView>(R.id.disconnection).isVisible = true
        findViewById<ImageView>(R.id.upload).isVisible = false
        findViewById<ImageView>(R.id.done).isVisible = false
        findViewById<ImageView>(R.id.sync).isVisible = false
    }

}