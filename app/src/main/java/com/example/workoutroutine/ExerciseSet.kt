package com.example.workoutroutine

import java.io.Serializable

data class ExerciseSet(val exerciseSetName: String, val exerciseElements: MutableList<Exercise>) : Serializable {}