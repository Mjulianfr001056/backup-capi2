package com.polstat.pkl.utils

class UtilFunctions {
    companion object {
        fun convertTo3DigitsString(number: Int?): String {
            return String.format("%03d", number)
        }
    }
}