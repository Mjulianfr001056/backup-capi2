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

        fun checkStringAngka(inputStr: String): String {
            // Cek apakah string adalah numerik atau berakhir dengan satu huruf kapital setelah karakter numerik
            return if (inputStr.matches(Regex("^[0-9]+[A-Z]?$"))) {
                inputStr
            } else {
                ""
            }
        }


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

        fun isValidNoSegmenFormat(input: String): Boolean {
            val regex = Regex("S[0-9]{3}")
            return regex.matches(input)
        }

        fun isDuplicateElement(list: List<String>, element: String): Boolean {
            return list.contains(element)
        }

        fun isDuplicateElementFromNestedList(nestedList: List<List<String>>, element: String) : Boolean {
            return nestedList.any { it.contains(element) }
        }

//        fun getIndex1AndIndex2FromNestedList(nestedList: List<List<String>>, element: String) : List<Int> {
//            var index1 = 0
//            var index2 = 0
//            var isFound = false
//
//            for (i in 0..nestedList.size - 1) {
//                for (j in 0..nestedList[i].size - 1) {
//                    if (nestedList[i][j] == element) {
//                        index2 = j
//                        isFound = true
//                        break
//                    }
//                }
//                if (isFound) {
//                    index1 = i
//                    break
//                }
//            }
//
//            if (!isFound) {
//                index1 = -1
//                index2 = -1
//            }
//
//            return listOf(index1, index2)
//        }

        fun getIndex1AndIndex2FromNestedList(nestedList: List<List<String>>, element: String) : List<Int> {
            for ((index1, list) in nestedList.withIndex()) {
                val index2 = list.indexOf(element)
                if (index2 != -1) {
                    return listOf(index1, index2)
                }
            }
            return listOf(-1, -1)
        }

    }
}