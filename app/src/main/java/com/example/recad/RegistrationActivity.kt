package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegistrationActivity : AppCompatActivity() {

    private lateinit var detector: GestureDetectorCompat
    private var frag: Fragment? = null

    private lateinit var signInButton: ImageView
    private lateinit var displayCalendar: ImageView
    private lateinit var editDate : EditText
    private val emailLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    private val isValidLiveData = MediatorLiveData<Boolean>().apply {
        this.value=false

        addSource(emailLiveData) { email ->
            // Monitors changes in email block
            val passw = passwordLiveData.value
            this.value = validateForm(email, passw)
        }

        addSource(passwordLiveData) { passw ->
            // Monitors changes in email block
            val email = emailLiveData.value
            this.value = validateForm(email, passw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        detector = GestureDetectorCompat(this, DiaryGestureListener())



        val spinner = findViewById<Spinner>(R.id.spinner)

        val lista = resources.getStringArray(R.array.options)

        // Crear adaptador visual para añadir cada uno de lso elementos al spinner
        val adaptador = ArrayAdapter(this, R.layout.custom_spinner, lista)
        adaptador.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner.adapter = adaptador

        //get value of spinner
        spinner.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(this@RegistrationActivity, "Elemento seleccionado: " + lista[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        editDate = findViewById(R.id.dateField)

        displayCalendar = findViewById(R.id.calendarImage)
        displayCalendar.setOnClickListener{
            showDatePickerDialog()
        }


        usernameField = findViewById(R.id.usernameField2)
        passwordField = findViewById(R.id.passwordField2)

            usernameField.doOnTextChanged { text, _, _, _ ->
                emailLiveData.value = text?.toString()
            }

            passwordField.doOnTextChanged { text, _, _, _ ->
                passwordLiveData.value = text?.toString()
            }

        signInButton = findViewById(R.id.registrationButton)

        isValidLiveData.observe(this) { isValid ->
            signInButton.isVisible = isValid
            signInButton.isEnabled = isValid
        }

        signInButton.setOnClickListener {
            // Once we check the parameters of the new user are correct, we create it into Firebase
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(usernameField.text.toString(),
                passwordField.text.toString()).addOnCompleteListener(this, OnCompleteListener { task ->

                val user = FirebaseAuth.getInstance().currentUser // check if user is logged in firebase android and then load another activity

        //notifies if the user has been created correctly
                    if(task.isSuccessful){
                        if (user != null) {
                            showWelcome(user)
                        }
                        Toast.makeText(this, "Successfully created account", Toast.LENGTH_SHORT).show()
                    } else {
                        showAlert()
                    }
            })

        }


    }


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("An error has occurred while creating the user")
        builder.setPositiveButton("Ok", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showWelcome(user: FirebaseUser){
        frag = supportFragmentManager.findFragmentById(R.id.container)
        if (frag == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment()).commit()
        }
    }


    private fun showDatePickerDialog(){
        val datePicker = CalendarFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")

    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        editDate.setText("$day/"+(month+1).toString() +"/$year")
    }

    private fun validateForm(email: String?, password:String?) : Boolean {
        val isValidEmail = email != null && email.isNotBlank() && email.contains("@")
        val isValidPassw = password != null && password.isNotBlank() && password.length >= 6
        return isValidEmail && isValidPassw
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if(event?.let { detector.onTouchEvent(it) } == true){
            true
        } else {
            super.onTouchEvent(event)
        }
    }
    inner class DiaryGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(downEvent: MotionEvent?, moveEvent: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

            var diffX = moveEvent?.x?.minus(downEvent!!.x) ?: 0.0F
            var diffY = moveEvent?.y?.minus(downEvent!!.y) ?: 0.0F

            return if(Math.abs(diffX) > Math.abs(diffY)){
                //this is a left or right swipe
                if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffX > 0){
                        //this@RegistrationActivity.onSwipeRight()
                    } else {
                        //this@RegistrationActivity.onSwipeLeft()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                // This is either a bottom or top swipe
                if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffY > 0){
                        this@RegistrationActivity.onSwipeTop()
                    } else {
                        //this@RegistrationActivity.onSwipeBottom()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }

    private fun onSwipeTop() {
        startActivity(Intent(this, MainActivity::class.java))
    }


}