package com.polstat.pkl.utils

class UtilFunctions {
    companion object {
        fun convertTo3DigitsString(number: Int?): String {
            return String.format("%03d", number)
        }

        fun convertTo4DigitsString(number: Int?): String {
            return String.format("%04d", number)
        }

        fun padWithZeros(input: String, length: Int = 4): String {
            return input.padStart(length, '0')
        }

    }
}