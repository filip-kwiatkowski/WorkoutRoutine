package com.example.workoutroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var userExerciseSets : MutableList<ExerciseSet> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val sitUp = Exercise("sitUp", 5, 67.5)
//        val pushUp = Exercise("pushUp", 9, 50.0)
//        val setA = ExerciseSet("Set A", mutableListOf(sitUp,pushUp))

//        val fos: FileOutputStream = this.openFileOutput("savedExerciseSet", MODE_PRIVATE)
//        val os = ObjectOutputStream(fos)
//       os.writeObject(setA)
//        os.close()
//        fos.close()

        val fis: FileInputStream = this.openFileInput("savedExerciseSet")
        val inStr = ObjectInputStream(fis)
        val setB: ExerciseSet = inStr.readObject() as ExerciseSet
        userExerciseSets.add(setB)
        inStr.close()
        fis.close()

        displayExercise(setB.exerciseElements.first())


        spinnerExerciseSet.adapter =
            ArrayAdapter(
                this,
                R.layout.spinner_item,
                resources.getStringArray(R.array.exercises)
            )

        spinnerExerciseSet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                parent.getItemAtPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            numberOfReps.text.replace(0, numberOfReps.text.length, getString(NUMBER_OF_REPS))
            numberOfKg.text.replace(0, numberOfKg.text.length, getString(NUMBER_OF_KG))
            timeLeft.text.replace(0, timeLeft.text.length, getString(TIMER))
        }

    }


    fun displayExercise(exercise: Exercise){
        exerciseName.text = exercise.exerciseName
        numberOfReps.text.replace(0, numberOfKg.text.length, exercise.numberOfReps.toString())
        numberOfKg.text.replace(0, numberOfKg.text.length, exercise.weight.toString())
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