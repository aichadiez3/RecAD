package com.example.recad

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class RecordingActivity : AppCompatActivity() {

    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var alert: TextView
    private lateinit var mr: MediaRecorder

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        startButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        alert = findViewById(R.id.alert)


        val sdf = SimpleDateFormat("dd-M-yyyy-hhmmss")
        val currentDate = sdf.format(Date())



        // We ask for recording permission
        if(ActivityCompat.checkSelfPermission(this@RecordingActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)

            val path:String = "/audioRecord/record-$currentDate.3gp"
            Files.createDirectory(Paths.get(path))
            //println(Paths.get(path).toAbsolutePath())

            val compPath:String = Environment.getExternalStorageDirectory().absolutePath.toString() + path // esto es el nombre del archivo a guardar
            val state = Environment.getExternalStorageState()


            mr = MediaRecorder()

            if (state != Environment.MEDIA_MOUNTED) {
                enableStart()
            } else {
                alert.text="No SD card was found"
            }



            startButton.setOnClickListener{

                mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // iS THE FORMAT FOR OPTIMIZING THE SPEECH CODING

                mr.setOutputFile(compPath)

                mr.prepare()
                mr.start()

                enableStop()

            }


            // To stop recording
            stopButton.setOnClickListener{
                mr.stop()
                mr.release()
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
