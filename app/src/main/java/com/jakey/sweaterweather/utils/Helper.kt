package com.jakey.sweaterweather.utils

object Helper {

    fun String.celToFahr() : String {
        val celsius = this.filter { it.isDigit() || it == '-' }.toDouble()
        val ans =  celsius * 9/5 + 32
        return ans.toInt().toString()  + " °F"
    }

    fun String.kphToMph(): String {
        val kmh = this.filter { it.isDigit() }.toDouble()
        val ans = (kmh / 1.609344).toInt()
        return "$ans mp/h"
    }
}