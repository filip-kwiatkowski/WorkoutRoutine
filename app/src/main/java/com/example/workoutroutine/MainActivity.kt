package com.example.workoutroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            numberOfReps.text.replace(0, numberOfReps.text.length, getString(NUMBER_OF_REPS))
            numberOfKg.text.replace(0, numberOfKg.text.length, getString(NUMBER_OF_KG))
            timer.text.replace(0, timer.text.length, getString(TIMER))
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun onAddOneRep(view: View) {
        if (numberOfReps.text.toString() != "") {
            val x = numberOfReps.text.toString().toInt() + 1
            numberOfReps.text.replace(0, numberOfReps.text.length, x.toString())
        }
    }

    fun onRemoveOneRep(view: View) {
        if (numberOfReps.text.toString() != "") {
            val x = numberOfReps.text.toString().toInt() - 1
            if (x > 0) numberOfReps.text.replace(0, numberOfReps.text.length, x.toString())
            else numberOfReps.text.replace(0, numberOfReps.text.length, "0")
        }
    }

    fun onAddOneKg(view: View) {
        if (numberOfReps.text.toString() != "") {
            val x = numberOfKg.text.toString().toDouble() + 1
            numberOfKg.text.replace(0, numberOfKg.text.length, x.toString())
        }
    }

    fun onRemoveOneKg(view: View) {
        if (numberOfReps.text.toString() != "") {
            val x = numberOfKg.text.toString().toDouble() - 1
            if (x > 0) numberOfKg.text.replace(0, numberOfKg.text.length, x.toString())
            else numberOfKg.text.replace(0, numberOfKg.text.length, "0")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString(NUMBER_OF_REPS, numberOfReps.text.toString())
            putString(NUMBER_OF_KG, numberOfKg.text.toString())
            putString(TIMER, timer.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val NUMBER_OF_KG = "userKg"
        const val NUMBER_OF_REPS = "userReps"
        const val TIMER = "userTimer"
    }
}