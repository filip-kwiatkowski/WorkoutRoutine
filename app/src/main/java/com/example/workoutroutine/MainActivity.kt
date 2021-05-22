package com.example.workoutroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            timeLeft.text.replace(0, timeLeft.text.length, getString(TIMER))
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
            putString(TIMER, timeLeft.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val NUMBER_OF_KG = "userKg"
        const val NUMBER_OF_REPS = "userReps"
        const val TIMER = "userTimer"
    }

    //    TODO(Can add better memorization of user-set time)
    //    TODO(Add reset button)
    private lateinit var restTimer: RestTimer
    private var userTimerValue = 0L
    fun onTimerStart(view: View) {
        if (timeLeft.text.toString() == "0:00")
            restTimer = RestTimer(timer, userTimerValue * 1000 + 1000)
        else {
            userTimerValue = convertTime(timeLeft.text.toString())
            restTimer = RestTimer(timer, userTimerValue * 1000 + 1000)
        }
        restTimer.start()
        timeLeft.isEnabled = false
        buttonStartTimer.isEnabled = false
        buttonStopTimer.isEnabled = true
    }

    fun onStopTimer(view: View) {
        timeLeft.isEnabled = true
        restTimer.cancel()
        buttonStartTimer.isEnabled = true
        buttonStopTimer.isEnabled = false
    }

    private fun convertTime(input: String): Long {
        return if (input.length > 3 && input[input.length - 3].compareTo(':') == 0) {
            val splitValue = input.split(":")
            (splitValue[0].toLong() * 60 + splitValue[1].toLong())
        } else if (!input.contains(":")) {
            input.toLong()
        } else {
            Toast.makeText(applicationContext, "Wrong input!", Toast.LENGTH_SHORT).show()
            0L
        }
    }
}