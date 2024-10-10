package dev.azv.zadanieabs.domain.user

import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.data.user.User

class SessionManagerImpl(
    private val userRepository: UserRepository = UserRepository()
) : SessionManager {
    private var userData: User? = null

    override fun getUser(): User? = userData

    override fun setUser(user: User?) {
        userData = user
    }
}