package com.example.recad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()

    private lateinit var settings: ImageView
    private lateinit var voice: ImageView
    private lateinit var records: ImageView
    private lateinit var logout: ImageView
    private lateinit var nameField: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        nameField = findViewById(R.id.nameText)

       //Get the information from the user from DB

        val username = intent.getStringExtra("email").toString()
        val name = intent.getStringExtra("name").toString()

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            database.collection("users").document(username).get().addOnSuccessListener {
                nameField.text = user.displayName


            }
        }



        voice = findViewById(R.id.voiceButton)
        voice.setOnClickListener {
            startActivity(Intent(this, VoiceMenuActivity::class.java))
        }

        settings = findViewById(R.id.settingsButton)
        settings.setOnClickListener {
            showDialog()
            //Toast.makeText(this, "Settings is unavailable", Toast.LENGTH_SHORT).show()
        }

        records = findViewById(R.id.recordsButton)
        records.setOnClickListener {
            Toast.makeText(this, "Records is unavailable", Toast.LENGTH_SHORT).show()
        }

        logout = findViewById(R.id.logoutButton)
        logout.setOnClickListener {

            // Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
            startActivity(Intent(this, MainActivity::class.java))
        }


/*
    // Guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() // Es un fichero compartido de preferencias del tipo clave-valor compartido en toda nuestra app
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        prefs.putString("email", email.toString())
        prefs.putString("provider", provider.toString())
        prefs.apply()

        //setup(email ?: "", provider ?: "")

 */
    }


    private fun showDialog(){
        val builder = AlertDialog.Builder(this@HomeActivity)
        builder.setTitle("Delete account")
        builder.setMessage("We are sorry to see you go. Are you sure you want to delete your account?")
            .setCancelable(false)
            .setPositiveButton("Delete"){ dialog, id ->

                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    database.collection("users").document(user.email.toString()).delete() // delete the collection associated to the registered email

                    user.delete().addOnCompleteListener {
                    Toast.makeText(this, "Account successfully deleted .", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }


            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


}