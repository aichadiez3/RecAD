package com.example.recad

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private lateinit var user : FirebaseUser
    private lateinit var delete: TextView
    private lateinit var nameField: EditText
    private lateinit var surnameField: EditText
    private lateinit var birthdayField: EditText
    private lateinit var genderField: String
    private lateinit var languageField: String
    private lateinit var editDate: EditText
    private lateinit var confirm: Button
    private lateinit var displayCalendar: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        user = FirebaseAuth.getInstance().currentUser!!


        // ------> Spinners management
        val spinnerGender = findViewById<Spinner>(R.id.genderSpinner)
        val lista1 = resources.getStringArray(R.array.options)
        val adaptador1 = ArrayAdapter(this, R.layout.custom_spinner, lista1)
        adaptador1.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinnerGender.adapter = adaptador1
            // ----------> get value of spinner
        spinnerGender.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(this@RegistrationActivity, "Elemento seleccionado: " + lista[position], Toast.LENGTH_LONG).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val spinnerLanguages = findViewById<Spinner>(R.id.languageSpinner)
        val lista2 = resources.getStringArray(R.array.languages)
        val adaptador2 = ArrayAdapter(this, R.layout.custom_spinner, lista2)
        adaptador2.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinnerLanguages.adapter = adaptador2

            // ----------> get value of spinner
        spinnerLanguages.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(this@RegistrationActivity, "Elemento seleccionado: " + lista[position], Toast.LENGTH_LONG).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        // ------> Fields identification
        nameField = findViewById(R.id.editName)
        surnameField = findViewById(R.id.editSurname)
        birthdayField = findViewById(R.id.dateField2)
        val group = findViewById<ChipGroup>(R.id.chipGroup)
        var antecedents = arrayListOf<String>()

        // -------> get external data
        database.collection("users").document(user.email.toString()).get().addOnSuccessListener { document ->
            if(document != null){
                nameField.text = Editable.Factory.getInstance().newEditable(document.get("name").toString())
                surnameField.text = Editable.Factory.getInstance().newEditable(document.get("surname").toString())
                birthdayField.text = Editable.Factory.getInstance().newEditable(document.get("date of birth").toString())
                spinnerGender.setSelection(adaptador1.getPosition(document.get("gender").toString()))
                spinnerLanguages.setSelection(adaptador2.getPosition(document.get("language").toString()))
                var doc = document.get("antecedents")
                if(doc == null){
                    antecedents.clear()
                }else{
                    antecedents = doc as ArrayList<String>
                }




            // -----> Falta que marque los spinners correspondientes a su id


            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error getting user info", Toast.LENGTH_SHORT).show()
        }



        delete = findViewById(R.id.deleteAccountButton)
        delete.setOnClickListener {
            showDialog()
        }

        editDate = findViewById(R.id.dateField2)
        displayCalendar = findViewById(R.id.calendarImage2)
        displayCalendar.setOnClickListener{
            showDatePickerDialog()
        }

        confirm = findViewById(R.id.confirmButton)
        confirm.setOnClickListener {

            languageField = spinnerLanguages.selectedItem.toString()
            genderField = spinnerGender.selectedItem.toString()

            val ids = group.checkedChipIds
            if(ids.isNotEmpty()){
                antecedents.clear()
                for(id in ids){
                    val text = group.findViewById<Chip>(id).text.toString()
                    antecedents.add(text)
                }
            } else {
                antecedents.clear()
            }

            val verif = saveInfo(nameField.text.toString(), surnameField.text.toString(), birthdayField.text.toString(), genderField, languageField, antecedents)
            if (verif){
                finish()
                Toast.makeText(this, "Data changed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error updating info", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun saveInfo(name:String, surname:String, date:String, gender:String, language:String, antec:ArrayList<String>): Boolean {

        database.collection("users").document(user.email.toString()).update("name", name)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating name", Toast.LENGTH_SHORT).show() }
        database.collection("users").document(user.email.toString()).update("surname", surname)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating surname", Toast.LENGTH_SHORT).show() }
        database.collection("users").document(user.email.toString()).update("date of birth", date)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating date of birth", Toast.LENGTH_SHORT).show() }
        database.collection("users").document(user.email.toString()).update("language", language)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating language", Toast.LENGTH_SHORT).show() }
        database.collection("users").document(user.email.toString()).update("gender", gender)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating gender", Toast.LENGTH_SHORT).show() }
        database.collection("users").document(user.email.toString()).update("antecedents", antec)
            .addOnFailureListener {
                Toast.makeText(this, "Error updating antecedents", Toast.LENGTH_SHORT).show() }

        val passw1 = Editable.Factory.getInstance().newEditable(findViewById<EditText>(R.id.changePassword).text)
        val passw2 = Editable.Factory.getInstance().newEditable(findViewById<EditText>(R.id.changePassword2).text)
        if (passw1.isNotEmpty() && passw2.isNotEmpty()) {
                changePassword(user, passw1, passw2)
        }


        return true

    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        editDate.setText("$day/"+(month+1).toString() +"/$year")
    }

    private fun showDatePickerDialog(){
        val datePicker = CalendarFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this@SettingsActivity)
        builder.setTitle("Delete account")
        builder.setMessage("We are sorry to see you go. Are you sure you want to delete your account?")
            .setCancelable(false)
            .setPositiveButton("Delete") { dialog, id ->

                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    database.collection("users").document(user.email.toString())
                        .delete() // delete the collection associated to the registered email

                    user.delete().addOnCompleteListener {
                        Toast.makeText(this, "Account successfully deleted", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }


            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun changePassword(user: FirebaseUser, oldPass: Editable, newPass: Editable){
        val credential = EmailAuthProvider.getCredential(user.email.toString(), oldPass.toString())
        user.reauthenticate(credential).addOnCompleteListener {  task ->
            if(task.isSuccessful){
                user.updatePassword(newPass.toString())
                    .addOnCompleteListener {
                        Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error changing the password", Toast.LENGTH_SHORT).show() }

            } else {
                Toast.makeText(this, "Error with user authentication", Toast.LENGTH_SHORT).show()
            }
        }

    }

}