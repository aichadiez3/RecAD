package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater.inflate(R.layout.welcome, container, true)


        Handler(Looper.getMainLooper()).postDelayed( {  // This method will be executed once the timer is over

        // We are opening an activity from a fragment
            // Because Fragment is NOT of Context type, we'll need to call the parent HomeActivity
            activity?.let{
                val intent = Intent (it, HomeActivity::class.java)
                it.startActivity(intent)
            }
        },2000) // value in milliseconds


        return inflater.inflate(R.layout.welcome, container, false)
        }
    }