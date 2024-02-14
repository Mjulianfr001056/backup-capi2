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

//        fun convertStringToNumber(str: String): Int {
//            if (str.isBlank() || str.startsWith('0')) return 0
//
//            val num = str.takeWhile { it.isDigit() }.toInt()
//            val letter = str.dropWhile { it.isDigit() }
//
//            return num + when (letter) {
//                "" -> 0
//                else -> letter.first().uppercaseChar() - 'A' + 1
//            }
//        }

        fun convertStringToNumber(str: String): Int {
            // Trim string dan periksa kasus khusus
            val trimmedStr = str.trimStart('0').trim() // Menghapus leading zeros dan whitespace
            if (trimmedStr.isBlank() || trimmedStr.startsWith('0')) return 0 // Mengembalikan 0 untuk string kosong atau "0"

            val numStr = trimmedStr.takeWhile { it.isDigit() } // Ekstrak bagian angka
            val num = numStr.toIntOrNull() ?: 0 // Konversi ke integer, default ke 0 jika tidak valid

            val letter = trimmedStr.dropWhile { it.isDigit() } // Ekstrak bagian huruf

            return num + when {
                letter.isEmpty() -> 0 // Tidak ada huruf, tidak ada penambahan
                letter.startsWith('0') -> 0 // Huruf dimulai dengan '0', dianggap tidak valid, jadi tetap 0
                else -> letter.first().uppercaseChar() - 'A' + 1 // Konversi huruf pertama ke nilai numerik
            }
        }


    }
}