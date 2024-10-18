package dev.azv.zadanieabs.domain.user

import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.data.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManagerImpl : SessionManager {
    private var userData: User? = null
    override val isLoggedIn: Boolean = userData != null

    override fun getUser(): User? = userData

    override fun setUser(user: User?) {
        userData = user
    }
}