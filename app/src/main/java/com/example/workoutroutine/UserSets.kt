package com.example.workoutroutine

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_sets.*

class UserSets : AppCompatActivity() {
    private lateinit var userExerciseSets: ArrayList<ExerciseSet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sets)

        userExerciseSets = intent.getSerializableExtra("userExerciseSets") as ArrayList<ExerciseSet>
        val arrayAdapter = ArrayAdapter(this, R.layout.list_exercise_set_item, userExerciseSets)
        userSetsListView.adapter = arrayAdapter

        userSetsListView.onItemClickListener = ListOnClickListener()
    }

    class ListOnClickListener : AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            //TODO("Not yet implemented - go to setEdit(maybe?)")
        }

        override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
            //TODO("Not yet implemented - delete set")
            return true
        }
    }

    fun onCreateNewSet(view: View) {
        val intent = Intent(this, SetCreator::class.java).putExtra("userExerciseSets", userExerciseSets)
        startActivity(intent)
    }
}