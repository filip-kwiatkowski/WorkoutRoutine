package com.example.workoutroutine

import java.io.Serializable

data class Exercise(val exerciseName: String, var numberOfReps: Int, var weight: Double) : Serializable {}