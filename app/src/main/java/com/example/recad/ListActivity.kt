package com.example.recad

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException


private const val LOG_TAG = "AudioRecordTest"
private const val TAG = "FirestoreDataBaseAccess"

class ListActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mStorage: StorageReference

    private lateinit var homeButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var list: ListView
    private lateinit var recordsList: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<*>

    private var referenceDir:String = ""
    private lateinit var localFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        homeButton = findViewById(R.id.homeButton)
        playButton = findViewById(R.id.play)
        pauseButton = findViewById(R.id.pause)
        list  = findViewById(R.id.list)



        user = FirebaseAuth.getInstance().currentUser!!

        database.collection("users").document(user.email.toString()).get().addOnSuccessListener { document ->
            var type = document?.get("diagnosis")?.toString().toString()
            referenceDir = "Records/$type"
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting type: ", exception)
        }
        // Charge data into the list from DB
        reloadAllData()


        list.setOnItemClickListener { _, _, position, _ ->

            var audioFound: Boolean
            var audioRef = recordsList[position]

            mStorage = FirebaseStorage.getInstance().getReference(referenceDir)
            val pathReference = mStorage.child("$audioRef") // Create a reference with an initial file path and name
            localFile = File.createTempFile(audioRef, "3gp")


            try {
                pathReference.getFile(localFile).addOnSuccessListener {
                    Toast.makeText(this, "Local temp file has been created: $localFile", Toast.LENGTH_SHORT).show()
                    audioFound = true

                    if (audioFound) {
                        enableReproduction()
                    } else {
                        disableAll()
                    }
                }.addOnFailureListener{ ex ->
                    Log.e(LOG_TAG, "Error creating local file $localFile", ex)
                }


            } catch(e: Exception){
                Log.e(LOG_TAG, "Error creating local file $localFile", e)
            }


            playButton.setOnClickListener {
                disableReproduction()
                startPlaying(localFile)
            }

            pauseButton.setOnClickListener {
                stopPlaying()
                disableAll()
                arrayAdapter.clear()
                reloadAllData()
            }
        }


        homeButton.setOnClickListener {
            finish()
        }


    }

    private fun reloadAllData() {
        recordsList = arrayListOf()

        database.collection("users").document(user.email.toString()).collection("records").get().addOnSuccessListener { documents ->
            for (doc in documents) {
                var recName = doc.data["record reference"].toString()
                (recordsList as ArrayList<String>).add(recName)
            }
            arrayAdapter = ArrayAdapter(this, R.layout.list_edit_text, recordsList)
            list.adapter = arrayAdapter

            if(recordsList.isNotEmpty()){
                list.isEnabled=true
                increaseAlpha(list)
            }

        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }

    }

    private fun startPlaying(filePath: File) {

        list.isEnabled = false
        decreaseAlpha(list)

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath.toString())
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Playing prepare() failed")
            }
            start()
        }

    }

    private fun stopPlaying() {
        mediaPlayer.pause()
        mediaPlayer.release()
    }




    private fun increaseAlpha(v: View){
        v.alpha = 1F
    }
    private fun decreaseAlpha(v: View){
        v.alpha = 0.5F
    }

    private fun enableReproduction(){
        playButton.isEnabled = true
        pauseButton.isEnabled = false
        decreaseAlpha(pauseButton)
        increaseAlpha(playButton)
    }
    private fun disableReproduction(){
        playButton.isEnabled = false
        pauseButton.isEnabled = true
        decreaseAlpha(playButton)
        increaseAlpha(pauseButton)
    }
    private fun disableAll() {
        decreaseAlpha(pauseButton)
        decreaseAlpha(playButton)
        playButton.isEnabled = false
        pauseButton.isEnabled = false
    }
}

