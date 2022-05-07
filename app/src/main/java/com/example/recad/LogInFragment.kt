package com.example.recad

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LogInFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.activity_main, container, true)

        username = view.findViewById(R.id.usernameField)
        password = view.findViewById(R.id.passwordField)
        loginButton = view.findViewById(R.id.loginButton)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {

            validateForm()


        }

        return view
    }

    private fun validateForm(){
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

                    markButtonEnable(loginButton)

                    var navRegister = activity as FragmentNavigation
                    navRegister.navigateFrag(HomeFragment(), false)

                } else {
                    username.setError("Enter a valid Email", icon)
                }
            }

        }

    }


    fun markButtonDisable(button: Button){
        button?.isEnabled = false
        button?.setBackgroundColor(ContextCompat.getColor(button.context, R.color.gray_light))
    }

    fun markButtonEnable(button: Button){
        button?.isEnabled = true
        button?.setBackgroundColor(ContextCompat.getColor(button.context, R.color.pink_dark))
    }

}