package com.example.chua_33520879.data.patient

fun isNumeric(str: String) = str.all { it in '0'..'9' }

fun ValidatePhoneNumber(value: String): Boolean {
    return isNumeric(value)
}