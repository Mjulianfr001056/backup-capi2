package com.polstat.pkl.utils.use_case

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if(password.isBlank())
            return ValidationResult(
                false,
                "Password tidak boleh kosong!"
            )
        return ValidationResult(
            true
        )
    }
}