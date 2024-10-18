package dev.azv.zadanieabs.data.user

import javax.inject.Singleton

@Singleton
interface SessionManager {
    fun getUser(): User?
    fun setUser(user: User?)
    val isLoggedIn: Boolean
}