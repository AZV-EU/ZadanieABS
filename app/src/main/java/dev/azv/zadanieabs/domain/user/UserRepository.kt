package dev.azv.zadanieabs.domain.user

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.azv.zadanieabs.data.database.FirebaseCallback
import dev.azv.zadanieabs.data.database.FirebaseResponse
import dev.azv.zadanieabs.data.user.User
import dev.azv.zadanieabs.common.Globals

class UserRepository (
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val userRef: DatabaseReference = rootRef.child(Globals.USERS_REF)
) {
    fun getUser(username: String, callback: FirebaseCallback) {
        userRef.equalTo(username, "username").get().addOnCompleteListener { task ->
            val response = FirebaseResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    print(result.childrenCount)
                    if (result.childrenCount > 0)
                        response.user = result.children.map { sp -> sp.getValue(User::class.java) }.first()
                }
            } else
                response.exception = task.exception
            callback.onResponse(response)
        }
    }
}