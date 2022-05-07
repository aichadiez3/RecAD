package com.example.recad

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var fAuth: FirebaseAuth
    private lateinit var registrationButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.activity_sign_in, container, true)
        username = view.findViewById(R.id.usernameField2)
        password = view.findViewById(R.id.passwordField2)
        registrationButton = view.findViewById(R.id.registrationButton)

        // We disable the button of registration until we check all parameters are valid
        markButtonDisable(registrationButton)

        // To edit the spinner
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item)
        //view.findViewById<Spinner>(R.id.genderSpinner).setAdapter(adapter)

        // We initialize Firebase
        fAuth = Firebase.auth

        //   ----------> This is how we change between fragments
        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LogInFragment(), false)
        }

        // This is to validate the parameters entered by the user
        view.findViewById<Button>(R.id.registrationButton).setOnClickListener {
            //validateEmptyForm()
        }

        view.findViewById<ImageView>(R.id.calendarImage).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(CalendarFragment(), false)
        }


        return inflater.inflate(R.layout.activity_sign_in,container,false)
    }
    /*

    private fun firebaseSignUp(){
        fAuth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener{
            task ->
            if(task.isSuccessful) {
                //Toast.makeText(context, "Successfuly registered", Toast.LENGTH_SHORT).show()
                var navHome = activity as FragmentNavigation // we use the interface to pass the method
                navHome.navigateFrag(HomeFragment(),true)
            } else {
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

     */

/*
    private fun validateEmptyForm(){
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.noun_error)

        icon?.setVisible(true, false)
        when {
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.setError("Please enter username", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.setError("Please enter password", icon)
            }

            username.text.toString().isNotEmpty() && password.text.isNotEmpty() -> {

                //Checks if the username follows the correct format of an email id
                if(username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
                    if(password.text.length >= 6){

                        markButtonEnable(registrationButton)
                        var navRegister = activity as FragmentNavigation
                        navRegister.navigateFrag(HomeFragment(), false)

                    } else {
                        password.setError("Password must have at least 6 characters", icon)
                    }
                } else {
                    username.setError("Enter a valid Email", icon)
                }
            }

        }

    }

 */


    fun markButtonDisable(button: Button){
        button?.isEnabled = false
        button?.setBackgroundColor(ContextCompat.getColor(button.context, R.color.gray_light))
    }

    fun markButtonEnable(button: Button){
        button?.isEnabled = true
        button?.setBackgroundColor(ContextCompat.getColor(button.context, R.color.pink_dark))
    }



}

