package dev.azv.zadanieabs.data.user

data class LoginResult (
    val userData: User? = null,
    val errorMessage: Int? = null
)