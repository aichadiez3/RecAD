package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), FragmentNavigation {

    //Intance that calls to database in Firebase
    private val database = FirebaseFirestore.getInstance()

    private lateinit var detector: GestureDetectorCompat
    private lateinit var loginButton: ImageView
    private lateinit var registerAccount: ImageView
    private lateinit var changePass: TextView
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private var touch: Int = 0

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
        setContentView(R.layout.activity_main)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Firebase integration completed")
        analytics.logEvent("InitScreen", bundle)



        detector = GestureDetectorCompat(this, DiaryGestureListener())


        registerAccount = findViewById(R.id.createAccount)
        registerAccount.setOnClickListener{
            // Opens a new activity for registration
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        changePass = findViewById(R.id.resetPasswordButton)
        changePass.setOnClickListener{
            Toast.makeText(this@MainActivity, "Change password unavailable", Toast.LENGTH_LONG).show()

        }

        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)

        usernameField.doOnTextChanged { text, _, _, _ ->
            emailLiveData.value = text?.toString()
        }

        passwordField.doOnTextChanged { text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        isValidLiveData.observe(this) { isValid ->
            loginButton.isVisible = isValid
            loginButton.isEnabled = isValid
        }


        loginButton.setOnClickListener {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameField.text.toString(),
                passwordField.text.toString()).addOnCompleteListener {
                //notifies if the user has been created correctly
                if(it.isSuccessful){
                    showHome()
                } else {
                    showAlert()
                }
            }


        }

    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("An error has occurred while authentication of the user")
        builder.setPositiveButton("Ok", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        startActivity(Intent(this, HomeActivity::class.java))
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

        // A fling event represents the user pressing on the screen + swiping up/down/left/right
        override fun onFling(
            downEvent: MotionEvent?,
            moveEvent: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            // This is how we measure the distance the user swipes to differ from accidental touch on the screen
                    // If var is null, returns 0.0F (a float)
            var diffX = moveEvent?.x?.minus(downEvent!!.x) ?: 0.0F
            var diffY = moveEvent?.y?.minus(downEvent!!.y) ?: 0.0F

            return if(Math.abs(diffX) > Math.abs(diffY)){
                //this is a left or right swipe
                if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffX > 0){
                        // Right swipe
                        this@MainActivity.onSwipeRight()
                    } else {
                        // Left swipe
                        this@MainActivity.onSwipeLeft()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                // This is either a bottom or top swipe
                if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffY > 0){
                        // Top swipe
                        this@MainActivity.onSwipeTop()
                    } else {
                        // Bottom swipe
                        this@MainActivity.onSwipeBottom()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }


        }
    }

    private fun onSwipeBottom() {
        Toast.makeText(this, "Bottom swipe", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeTop() {
        Toast.makeText(this, "Top swipe", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeRight() {

        touch++

        var frag = supportFragmentManager.findFragmentById(R.id.container)
        if (frag == null) {

            frag = DescriptionFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, frag)
                //.add(R.id.container, DescriptionFragment())   --> Also valid if we remove the variable frag declaration
                .commit()

        } else {
            frag = AboutUsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, frag)
                .commit()
        }
        if(touch==3){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun onSwipeLeft() {

        touch++

        var frag = supportFragmentManager.findFragmentById(R.id.container)

        if (frag == null) {

            frag = AboutUsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, frag)
                .commit()

        } else {

            frag = DescriptionFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, frag)
                .commit()

        }
        if(touch==3){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun navigateFrag(fragment: Fragment, addToStack: Boolean){
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if(addToStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


}