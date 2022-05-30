package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class RegistrationActivity : AppCompatActivity() {

    private lateinit var detector: GestureDetectorCompat

    private lateinit var signInButton: ImageView
    private lateinit var displayCalendar: ImageView
    private lateinit var editDate : EditText

    private var frag: Fragment? = null

    private val emailLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()

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

        signInButton = findViewById(R.id.registrationButton)
        signInButton.setOnClickListener {
            frag = supportFragmentManager.findFragmentById(R.id.container)
            if (frag == null) {
                supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment()).commit()
            }
        }

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


        val usernameField = findViewById<EditText>(R.id.usernameField2)
        val passwordField = findViewById<EditText>(R.id.passwordField2)

        usernameField.doOnTextChanged { text, _, _, _ ->
            emailLiveData.value = text?.toString()
        }

        passwordField.doOnTextChanged { text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        isValidLiveData.observe(this) { isValid ->
            signInButton.isVisible = isValid
            signInButton.isEnabled = isValid
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
        return if(detector.onTouchEvent(event)){
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