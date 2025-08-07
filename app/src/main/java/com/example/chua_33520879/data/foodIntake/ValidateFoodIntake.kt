package com.example.chua_33520879.data.foodIntake

import android.util.Log
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.absoluteValue

class ValidateFoodIntake {
    var ok: Boolean = true
    var msg: String = ""

    fun setOk() {
        ok = true
        msg = ""
    }

    fun setError(errorMsg: String) {
        ok = false
        msg = errorMsg
    }

    fun validate(data: FoodIntake) {
        if (data.persona.isEmpty()) {
            setError("Please select a persona that best fits you")
            return
        }

        if (data.wakeUpTiming.isEmpty() || data.biggestMealTiming.isEmpty() || data.sleepTiming.isEmpty()) {
            setError("Please ensure all fields in timings are properly filled up")
            return
        }

        val wakeUp = LocalTime.parse(data.wakeUpTiming, DateTimeFormatter.ofPattern("HH:mm"))
        val biggestMeal = LocalTime.parse(data.biggestMealTiming, DateTimeFormatter.ofPattern("HH:mm"))
        val sleep = LocalTime.parse(data.sleepTiming, DateTimeFormatter.ofPattern("HH:mm"))

        if (wakeUp == sleep) {
            setError("Invalid wake up and sleep timing")
            return
        }

        if (wakeUp == biggestMeal) {
            setError("Invalid wake up and biggest meal timing")
            return
        }

        if (sleep == biggestMeal) {
            setError("Invalid sleeping and biggest meal timing")
            return
        }


        if (wakeUp <= sleep) {
            // cross the next day
            if (Duration.between(sleep, wakeUp).toHours().absoluteValue < 1) {
                setError("Invalid sleeping duration, duration must be more than or equals to 1 hour!")
                return
            }

            if (biggestMeal >= sleep || biggestMeal <= wakeUp) {
                setError("Biggest meal timing must not be within the duration you sleep!")
                return
            }
        } else {
            // same day
            if (Duration.between(sleep, wakeUp).toHours() < 1) {
                setError("Invalid sleeping duration, duration must be more than or equals to 1 hour!")
                return
            }

            if (biggestMeal >= sleep && biggestMeal <= wakeUp) {
                setError("Biggest meal timing must not be within the duration you sleep!")
                return
            }
        }

        setOk()
    }
}