package com.example.recad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var saveDate: Button
    private lateinit var calendarView: CalendarView
    private lateinit var text: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.calendar_view, container, true)

        val calendar = Calendar.getInstance()

        saveDate = view.findViewById<Button>(R.id.saveDate)
        calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        text = view.findViewById<TextView>(R.id.textView3)




        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // We save the selected date to be accessed by RegistrationFragment contoller
            calendar.set(year,month,dayOfMonth)
            calendarView.date = calendar.timeInMillis
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            text.append(dateFormatter.format(calendar.time))

        }




        saveDate.setOnClickListener {
            val selectedDate:Long = calendarView.date
            calendar.timeInMillis = selectedDate

            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            text.append(dateFormatter.format(calendar.time))

            println(text.toString())



        }


        return inflater.inflate(R.layout.calendar_view, container, false)
    }



}