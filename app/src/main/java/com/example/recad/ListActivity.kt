package com.example.recad

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException


private const val LOG_TAG = "AudioRecordTest"
private const val TAG = "FirestoreDataBaseAccess"

class ListActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var homeButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var list: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        homeButton = findViewById(R.id.homeButton)
        playButton = findViewById(R.id.play)
        pauseButton = findViewById(R.id.pause)
        list = findViewById(R.id.listView)

        var audioRef = ""
        var audioFound = ""
        var recordsList: ArrayList<String> = arrayListOf()


        user = FirebaseAuth.getInstance().currentUser!!





        database.collection("users").document(user.email.toString()).collection("records").get().addOnSuccessListener { documents ->
            for(doc in documents){
                recordsList.add(doc.data["record reference"].toString())
            }
            Toast.makeText(this@ListActivity, recordsList.toString(), Toast.LENGTH_SHORT).show()

        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordsList)
        list.adapter = adapter

        list.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                Toast.makeText(this@ListActivity, "Has pulsado: " + recordsList.get(position), Toast.LENGTH_SHORT).show()

                audioRef = recordsList.get(position)


            }



        homeButton.setOnClickListener {
            finish()
        }


        if(audioRef == audioFound){
            playButton.isVisible = true
            pauseButton.isVisible = true
            pauseButton.isEnabled = false
        }


        playButton.setOnClickListener {
            startPlaying(audioRef)
            playButton.isEnabled = false
        }

        pauseButton.setOnClickListener {
            //stopPlaying()

        }


    }

    private fun startPlaying(filePath:String) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath)
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

}