package com.polstat.pkl.utils.use_case

class ValidateNim {

    companion object {
        const val NIM_VALIDATION_REGEX = "^\\d{9}\$"
    }

    fun execute(nim: String): ValidationResult {
        if(nim.isBlank())
            return ValidationResult(
                false,
                "NIM tidak boleh kosong!"
            )
        if(!nim.matches(Regex(NIM_VALIDATION_REGEX)))
            return ValidationResult(
                false,
                "NIM tidak valid!"
            )
        return ValidationResult(
            true
        )
    }
}