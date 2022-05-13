package com.example.recad

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*

class RecordingActivity : AppCompatActivity() {

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var alert: TextView
    private lateinit var mr: MediaRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        alert = findViewById(R.id.alert)


        val sdf = SimpleDateFormat("dd-M-yyyy-hh_mm_ss")
        val currentDate = sdf.format(Date())
        println("Date is ----> "+currentDate)

        var path:String = Environment.getExternalStorageDirectory().toString() + "/recording-"+currentDate.toString()+".3gp" // esto es el nombre del archivo a guardar
        mr = MediaRecorder()

        // We ask for recording permission
        if(ActivityCompat.checkSelfPermission(this@RecordingActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)

            enableStart()

            startButton.setOnClickListener{
                mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // iS THE FORMAT FOR OPTIMIZING THE SPEECH CODING

                mr.setOutputFile(path)

                mr.prepare()
                mr.start()

                enableStop()

            }


            // To stop recording
            stopButton.setOnClickListener{
                mr.stop()
                enableStart()

            }

        /*
            // Play de recording
            button.setOnClickListener {
                var mp = MediaPlayer()
                mp.setDataSource(path)
                mp.prepare()
                mp.start()

            }

             */

        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            enableStart()
        }
    }

    private fun enableStart(){
        startButton.isVisible = true
        stopButton.isVisible = false
        alert.text = "Press to start recording"
    }
    private fun enableStop(){
        startButton.isVisible = false
        stopButton.isVisible = true
        alert.text = "Press to stop recording"
    }




}
