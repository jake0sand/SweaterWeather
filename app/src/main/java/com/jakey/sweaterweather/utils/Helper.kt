package com.jakey.sweaterweather.utils

object Helper {

    fun String.celToFahr() : String {
        val celsius = this.filter { it.isDigit() || it == '-' }.toInt()
        val ans =  celsius * 9/5 + 32
        return ans.toString()  + " °F"
    }

    fun String.kphToMph(): String {
        val kmh = this.filter { it.isDigit() }.toDouble()
        val ans = (kmh / 1.609344).toInt()
        return "$ans mph"
    }
}