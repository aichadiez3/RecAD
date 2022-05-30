package com.example.recad

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class CalendarFragment (val listener: (day:Int, month:Int, year:Int) -> Unit): DialogFragment(), DatePickerDialog.OnDateSetListener {


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val yearCal  = Calendar.getInstance() // create yearCal instance  and -100 years
        yearCal.add(Calendar.YEAR, -100)

        val picker = DatePickerDialog(activity as Context, R.style.datePickerTheme, this, year, month, day)

        picker.datePicker.minDate = yearCal.timeInMillis
        picker.datePicker.maxDate = calendar.timeInMillis
        return picker
    }



}