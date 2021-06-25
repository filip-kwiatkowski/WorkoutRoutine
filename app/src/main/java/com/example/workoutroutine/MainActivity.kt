package com.example.workoutroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var userExerciseSets: ArrayList<ExerciseSet> = arrayListOf()
    lateinit var currentSet: ExerciseSet
    lateinit var currentExercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isFilePresent("savedExerciseSet")) {
            val squat = Exercise("Squat", 5, 50.0)
            val benchPress = Exercise("Bench Press", 5, 40.0)
            val rowing = Exercise("Rowing", 5, 40.0)
            val fly = Exercise("Fly", 8, 5.0)
            val biceps = Exercise("Biceps", 8, 7.5)
            val triceps = Exercise("Triceps", 8, 10.0)
            val sitUp = Exercise("Sit Up", 8, 0.0)
            val calves = Exercise("Calves", 8, 10.0)
            val forearm = Exercise("Forearm", 12, 20.0)
            val defaultSeT = ExerciseSet(
                "Default Set",
                arrayListOf(squat, benchPress, rowing, fly, biceps, triceps, forearm, sitUp, calves), 120
            )
            userExerciseSets.add(defaultSeT)

            val fos: FileOutputStream = this.openFileOutput("savedExerciseSet", MODE_PRIVATE)
            val os = ObjectOutputStream(fos)
            os.writeObject(userExerciseSets)
            os.close()
            fos.close()
        }

        val fis: FileInputStream = this.openFileInput("savedExerciseSet")
        val inStr = ObjectInputStream(fis)
        userExerciseSets = inStr.readObject() as ArrayList<ExerciseSet>
        inStr.close()
        fis.close()

        val exerciseSetsNameList = arrayListOf<String>()
        for (set in userExerciseSets) {
            exerciseSetsNameList.add(set.exerciseSetName)
        }


        spinnerExerciseSet.adapter =
            ArrayAdapter(
                this,
                R.layout.spinner_item,
                exerciseSetsNameList
            )

        spinnerExerciseSet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentSet = userExerciseSets.elementAt(position)
                currentExercise = currentSet.exerciseElements.first()
                displayExercise(currentExercise)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    private fun isFilePresent(fileName: String): Boolean {
        val path = this.filesDir.toString() + "/" + fileName
        val file = File(path)
        return file.exists()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            numberOfReps.text.replace(0, numberOfReps.text.length, getString(NUMBER_OF_REPS))
            numberOfKg.text.replace(0, numberOfKg.text.length, getString(NUMBER_OF_KG))
            timeLeft.text.replace(0, timeLeft.text.length, getString(TIMER))
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

    override fun onResume() {
        super.onResume()

        val fis: FileInputStream = this.openFileInput("savedExerciseSet")
        val inStr = ObjectInputStream(fis)
        userExerciseSets = inStr.readObject() as ArrayList<ExerciseSet>
        inStr.close()
        fis.close()
    }

    override fun onPause() {
        super.onPause()

        saveChangesOfCurrentExercise()
        val fos: FileOutputStream = this.openFileOutput("savedExerciseSet", MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(userExerciseSets)
        os.close()
        fos.close()
    }

    private fun displayExercise(exercise: Exercise) {
        exerciseName.text = exercise.exerciseName
        numberOfReps.text.replace(0, numberOfReps.text.length, exercise.numberOfReps.toString())
        numberOfKg.text.replace(0, numberOfKg.text.length, exercise.weight.toString())

        val min = currentSet.timer / 60
        val sec = currentSet.timer % 60
        val timerString = if (sec < 10)  "$min:0$sec" else "$min:$sec"

        timeLeft.text.replace(0, timeLeft.text.length, timerString)
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

    //TODO(Can add better memorization of user-set time)

    private lateinit var restTimer: RestTimer
    private var userTimerValue = 0L

    fun onTimerStart(view: View) {
        if (timeLeft.text.toString() == "0:00")
            restTimer = RestTimer(timer, userTimerValue * 1000 + 500)
        else {
            userTimerValue = convertTime(timeLeft.text.toString())
            restTimer = RestTimer(timer, userTimerValue * 1000 + 500)
        }
        restTimer.start()
        timeLeft.isEnabled = false
        buttonStartTimer.isEnabled = false
        buttonStopTimer.isEnabled = true
        buttonResetTimer.isEnabled = false
    }

    fun onStopTimer(view: View) {
        timeLeft.isEnabled = true
        restTimer.cancel()
        buttonStopTimer.isEnabled = false
        buttonResetTimer.isEnabled = true
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

    fun onResetTimer(view: View) {
        buttonStartTimer.isEnabled = true
        val min = userTimerValue / 60
        val sec = userTimerValue % 60
        if (sec < 10) timeLeft.text.replace(0, timeLeft.text.length, "$min:0$sec")
        else timeLeft.text.replace(0, timeLeft.text.length, "$min:$sec")

    }

    fun onPreviousExercise(view: View) {
        val index = currentSet.exerciseElements.indexOf(currentExercise)
        if (index > 0) {
            saveChangesOfCurrentExercise()
            currentExercise = currentSet.exerciseElements[index - 1]
            displayExercise(currentExercise)
        } else Toast.makeText(this, "No previous exercises", Toast.LENGTH_SHORT).show()
    }

    fun onNextExercise(view: View) {
        val index = currentSet.exerciseElements.indexOf(currentExercise)
        if (index < currentSet.exerciseElements.count() - 1) {
            saveChangesOfCurrentExercise()
            currentExercise = currentSet.exerciseElements[index + 1]
            displayExercise(currentExercise)
        } else Toast.makeText(this, "No next exercises", Toast.LENGTH_SHORT).show()
    }

    private fun saveChangesOfCurrentExercise() {
        currentExercise.numberOfReps = numberOfReps.text.toString().toInt()
        currentExercise.weight = numberOfKg.text.toString().toDouble()
        currentSet.timer = convertTime(timeLeft.text.toString())
    }

    //TODO(Add set creator)
    fun onYourSet(view: View) {
        val intent = Intent(this, UserSets::class.java).putExtra("userExerciseSets", userExerciseSets)
        startActivity(intent)
    }
}