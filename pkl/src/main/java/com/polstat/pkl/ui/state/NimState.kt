package com.polstat.pkl.ui.state

import java.util.regex.Pattern

private const val NIM_VALIDATION_REGEX = "\\d+"

@Deprecated("Pakai LoginScreenState")
class NimState(val nim: String? = null) :
    TextFieldState(validator = ::isNimValid, errorFor = ::nimValidationError) {
    init {
        nim?.let {
            text = it
        }
    }
}

private fun nimValidationError(nim: String): String {
    return "NIM tidak valid!"
}

private fun isNimValid(nim: String): Boolean {
    return Pattern.matches(NIM_VALIDATION_REGEX, nim)
}

val NimStateSaver = textFieldStateSaver(NimState())