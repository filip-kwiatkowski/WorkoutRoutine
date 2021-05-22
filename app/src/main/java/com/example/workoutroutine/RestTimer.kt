package com.example.workoutroutine


import android.os.CountDownTimer
import android.widget.EditText
import android.widget.LinearLayout

class RestTimer(private val id: LinearLayout, millisInFuture: Long = 60000, countDownInterval: Long = 1000) :
    CountDownTimer(millisInFuture, countDownInterval) {
    private var minutes = 0
    private var seconds = 0
    override fun onTick(millisUntilFinished: Long) {
        val editText = id.getChildAt(0) as EditText
        seconds = (millisUntilFinished / 1000).toInt() % 60
        minutes = (millisUntilFinished / 1000).toInt() / 60
        if (seconds<10) editText.text.replace(0, editText.text.length, "$minutes:0$seconds")
        else editText.text.replace(0, editText.text.length, "$minutes:$seconds")
    }

    override fun onFinish() {
        id.getChildAt(0).isEnabled = true
        id.getChildAt(1).isEnabled = true
        id.getChildAt(2).isEnabled = false
    }


}