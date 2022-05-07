package com.example.recad

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), FragmentNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a new fragment using the manager
        var frag = supportFragmentManager
            .findFragmentById(R.id.container)

        // Check the fragment has not already been initialized
        if (frag == null) {
            // Initialize the fragment based on our SimpleFragment
                frag = LogInFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, frag)
                            //.add(R.id.container, LogInFragment())   --> Also valid if we remove the variable frag declaration
                        .commit()
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