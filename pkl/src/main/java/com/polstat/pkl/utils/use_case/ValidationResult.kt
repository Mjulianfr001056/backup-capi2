package com.polstat.pkl.utils.use_case

data class ValidationResult (
    val successful: Boolean,
    val errorMessage: String? = null
)