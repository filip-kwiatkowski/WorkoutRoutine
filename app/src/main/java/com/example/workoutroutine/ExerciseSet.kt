package com.example.workoutroutine

import java.io.Serializable

data class ExerciseSet(val exerciseSetName: String, val exerciseElements: ArrayList<Exercise>, var timer: Long) : Serializable {
    override fun toString(): String {
        return exerciseSetName
    }
}