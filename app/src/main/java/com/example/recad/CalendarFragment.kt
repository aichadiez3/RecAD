package com.example.recad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class CalendarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.calendar_view, container, true)

        view.findViewById<Button>(R.id.saveDate).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegistrationFragment(), false)
           
        }

        return inflater.inflate(R.layout.calendar_view, container, false)
    }
}