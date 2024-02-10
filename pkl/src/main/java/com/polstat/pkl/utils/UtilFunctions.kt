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

        fun convertStringToNumber(str: String): Int {
            if (str.isBlank() || str.startsWith('0')) return 0

            val num = str.takeWhile { it.isDigit() }.toInt()
            val letter = str.dropWhile { it.isDigit() }

            return num + when (letter) {
                "" -> 0
                else -> letter.first().uppercaseChar() - 'A' + 1
            }
        }

    }
}