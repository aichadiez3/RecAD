package com.example.recad

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
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
import java.text.SimpleDateFormat
import java.util.*

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200


class RecordingActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var alert: TextView

    private var isRecording: Boolean = false
    private var filePath: String = ""
    private var recordRef: String = ""
    private var currentDate: String = ""

    private lateinit var mediaRecorder: MediaRecorder
    private var player: MediaPlayer? = null
    lateinit var mStorage : StorageReference

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)



    @SuppressLint("SimpleDateFormat")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)


        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        alert = findViewById(R.id.alert)

        mediaRecorder = MediaRecorder()


        var type = ""
        user = FirebaseAuth.getInstance().currentUser!!
        database.collection("users").document(user.email.toString()).get().addOnSuccessListener { document ->
            type = document?.get("diagnosis")?.toString().toString()

        }

        filePath = Environment.getExternalStorageDirectory().absolutePath

        try{
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            enableStart()
        } catch(e: Exception){
            alert.text = "Permissions denied"
            Toast.makeText(this, "permissions failed" , Toast.LENGTH_SHORT).show()
            //Log.e(LOG_TAG, "permissions failed")
            finish()
        }


        // We initialize the cloud storage to child location records and the diagnosis type
        mStorage = FirebaseStorage.getInstance().getReference("Records/$type")

        startButton.setOnClickListener {
            val sdf = SimpleDateFormat("dd-M-yyyy-hhmmss")
            currentDate = sdf.format(Date())
            recordRef = "record-$currentDate.3gp"

            filePath += "/$recordRef"
            startRecording(filePath)

        }

        stopButton.setOnClickListener {
            stopRecording()
            saveIntoCloudFirestore()
            uploadAudio(type, recordRef)
        }

    }


    private fun uploadAudio(type: String, audioFile:String){

        alert.text = "Uploading audio..."

        var path = mStorage.child(type).child(audioFile)
        var uri = Uri.fromFile(File(filePath))

        path.putFile(uri).addOnSuccessListener {
            alert.text = "Uploading Finished"
            waitBeforeClose()
        }
    }


    private fun saveIntoCloudFirestore(){

        alert.text = "Uploading References..."

        database.collection("users").document(user.email.toString()).collection("records").document(recordRef).set(
            hashMapOf("record reference" to recordRef,
                "record date" to currentDate)
        ).addOnSuccessListener {
            Toast.makeText(this, "Collection Records created", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to create document for $recordRef", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }


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
                Log.e(LOG_TAG, "start recording prepare() failed")
                isRecording = false
            }

            start()
            enableStop()

        }
    }

    private fun stopRecording() {

        if(isRecording){
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false

        } else {
            mediaRecorder.release()
        }

        enableStart()

    }

    private fun startPlaying(filePath:String) {
        var player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Playing prepare() failed")           }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }
/*
    private fun terminateAndEraseFile(context: Context) {
        Log.d(Constants.TAG, "RecordService terminateAndEraseFile")
        stopRecording()
        recording = false
        deleteFile()
    }

    private fun deleteFile() {
        Log.d(Constants.TAG, "RecordService deleteFile")
        FileHelper.deleteFile(fileName)
        fileName = null
    }

 */

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


    private fun waitBeforeClose(){

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            finish()
        }, 1500)

    }
}
