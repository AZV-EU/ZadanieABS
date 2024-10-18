package dev.azv.zadanieabs.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun tryLogin(username: String, password: String): Flow<LoginResult>
}