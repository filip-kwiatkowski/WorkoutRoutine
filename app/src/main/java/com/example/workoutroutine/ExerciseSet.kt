package com.example.workoutroutine

import java.io.Serializable

data class ExerciseSet(val exerciseSetName: String, val exerciseElements: List<Exercise>) : Serializable {}