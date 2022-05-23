package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class RegistrationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var signInButton: Button
    private lateinit var displayCalendar: ImageView

    private var frag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signInButton = findViewById(R.id.registrationButton)
        signInButton.setOnClickListener {

            frag = supportFragmentManager.findFragmentById(R.id.container)
            if (frag == null) {

                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WelcomeFragment())
                    .commit()

            }
        }

        val spinner = findViewById<Spinner>(R.id.spinner)

        //val lista = listOf("Male","Female")
        val lista = resources.getStringArray(R.array.options)

        // Crear adaptador visual para añadir cada uno de lso elementos al spinner
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        spinner.adapter = adaptador

        //get value of spinner
        spinner.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@RegistrationActivity, "Elemento seleccionado: " + lista[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        displayCalendar = findViewById(R.id.calendarImage)
        displayCalendar.setOnClickListener{

            var frag = supportFragmentManager.findFragmentById(R.id.container)
            if (frag == null) {

                supportFragmentManager.beginTransaction()
                    .add(R.id.container, CalendarFragment())
                    .commit()

            }



        }

    }

/*
    fun changeActivity(view: View){

        startActivity(Intent(this, LogInActivity::class.java).apply {
            // To pass any data to next activity
            putExtra("extra_1", value1)
            putExtra("extra_2", value2)
            putExtra("extra_3", value3)
        })

        button.setOnClickListener {
            val intent = Intent(this@MainActivity,SecondActivity::class.java);
            var userName = username.text.toString()
            var password = password_field.text.toString()
            intent.putExtra("Username", userName)
            intent.putExtra("Password", password)
            startActivity(intent);
        }
 }*/



}