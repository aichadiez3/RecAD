package com.example.recad

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), FragmentNavigation {

    private lateinit var detector: GestureDetectorCompat

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

        detector = GestureDetectorCompat(this, DiaryGestureListener())

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
        Toast.makeText(this, "Right swipe", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeLeft() {
        Toast.makeText(this, "Left swipe", Toast.LENGTH_LONG).show()
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