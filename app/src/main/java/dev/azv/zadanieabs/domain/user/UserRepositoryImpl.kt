package dev.azv.zadanieabs.domain.user

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.azv.zadanieabs.R
import dev.azv.zadanieabs.common.Constants
import dev.azv.zadanieabs.data.user.LoginResult
import dev.azv.zadanieabs.data.user.User
import dev.azv.zadanieabs.data.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

class UserRepositoryImpl @Inject internal constructor() : UserRepository {
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val userRef: DatabaseReference = rootRef.child(Constants.USERS_REF)

    override fun tryLogin(username: String, password: String): Flow<LoginResult> = flow {
        val request = userRef.orderByChild("username").equalTo(username).get()
        request.await()

        if (request.isSuccessful)
        {
            val user = request.result.children.first().getValue(User::class.java)!!

            if (BCrypt.checkpw(password, user.password))
                emit(LoginResult(userData = user))
            emit(LoginResult(errorMessage = R.string.login_failed))
        } else {
            Log.e(Constants.LOG_TAG, request.exception?.stackTraceToString() ?: "Unknown Firebase error")
            emit(LoginResult(errorMessage = R.string.login_failed_database_error))
        }
    }.catch { exception ->
        Log.e(Constants.LOG_TAG, exception.stackTraceToString())
        emit(LoginResult(errorMessage = R.string.login_failed_internal_error))
    }
}