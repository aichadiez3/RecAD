package com.example.recad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class RecordingActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var alert: TextView
    private lateinit var mediaRecorder: MediaRecorder
    private var permissionMic: Boolean = false
    private var state: Boolean = false
    private var path: String = ""


    private lateinit var user : FirebaseUser

    val AUDIO: Int = 0
    lateinit var uri : Uri // Holds file google address
    lateinit var mStorage : StorageReference
    lateinit var urlPath: String


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        alert = findViewById(R.id.alert)

        mediaRecorder = MediaRecorder()             /**         AUDIO AND STORAGE CONFIGURATION         */



        var type = ""
        user = FirebaseAuth.getInstance().currentUser!!
        database.collection("users").document(user.email.toString()).get().addOnSuccessListener { document ->
            type = document?.get("diagnosis")?.toString().toString()
        }

        // We initialize the cloud storage to child location records and the diagnosis type
        mStorage = FirebaseStorage.getInstance().getReference("Records/$type")


        // We ask for recording permissions
        if(ActivityCompat.checkSelfPermission(this@RecordingActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
            permissionMic = true
            alert.text = "Permissions okay"

        }

            /**
                // FOR EXTERNAL STORAGE
                val record = "record-$currentDate.3gp"
                var path = "$record"
                Files.createDirectory(Paths.get(path))

                val compPath:String = Environment.getExternalStorageDirectory().absolutePath.toString() + path // esto es el nombre del archivo a guardar
                val state = Environment.getExternalStorageState()

                if (state != Environment.MEDIA_MOUNTED) {
                    enableStart()
                } else {
                    alert.text="No SD card was found"
                }

             */


            startButton.setOnClickListener {
                Toast.makeText(this, mStorage.path.toString(), Toast.LENGTH_SHORT).show()
                this.state = startRecording(mStorage.path)

            }


            // To stop recording
            stopButton.setOnClickListener{ view: View? -> val it: Intent? = null

                if(this.state){
                    stopRecording(this.state)
                } else {
                    Toast.makeText(this, "Recording not started!", Toast.LENGTH_SHORT).show()
                }

                // Here we create the reference associated to the patient
                saveIntoCloudFirestore()


                it?.type = "audio/*"
                it?.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(it, "Create Audio"), AUDIO)



            }






    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            enableStart()
        }
    }



    @SuppressLint("SimpleDateFormat")
    private fun saveIntoCloudFirestore(){
        val sdf = SimpleDateFormat("dd-M-yyyy-hhmmss")
        val currentDate = sdf.format(Date())
        val recordRef = "record-$currentDate.3gp"

        database.collection("users").document(user.email.toString()).collection("records").document(recordRef).set(
            hashMapOf("record reference" to recordRef,
                "record date" to currentDate)
        ).addOnSuccessListener {
            Toast.makeText(this, "Collection Records created", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to create document for $recordRef", Toast.LENGTH_LONG).show()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == RESULT_OK){
            if(requestCode == AUDIO){
                uri = data!!.data!!
                upload()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload(): String{
        try {
            var mReference = uri.lastPathSegment?.let {
                mStorage.child(it) } // Contents last path segment
            var url = ""
            mReference?.putFile(uri)
                ?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    url = taskSnapshot!!.metadata!!.reference!!.downloadUrl.toString()
                    Toast.makeText(this, "Successfully uploaded to Cloud. Url: $url", Toast.LENGTH_LONG).show()
                }
            return url
        } catch(e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            return "Error in Upload to Cloud!"
        }
    }

    private fun startRecording(filePath: String): Boolean{

        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // iS THE FORMAT FOR OPTIMIZING THE SPEECH CODING
            mediaRecorder.setOutputFile(filePath)
            mediaRecorder.prepare()
            mediaRecorder.start()

            enableStop()

            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            return true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    private fun stopRecording(state: Boolean){
        if(state){
            mediaRecorder.stop()
            mediaRecorder.reset()
            mediaRecorder.release()
            Toast.makeText(this, "Record has stopped. Registering into Cloud...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playRecording(urlPath: String){
            var mp = MediaPlayer()
            mp.setDataSource(urlPath)
            mp.prepare()
            mp.start()
    }

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
        alert.text = "Press to stop recording"
    }

}
