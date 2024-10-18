package dev.azv.zadanieabs.data.database

sealed class FirebaseResponse<T> {
    data class Success<T>(val data: T): FirebaseResponse<T>()
    data class Error<T>(val exception: Exception?): FirebaseResponse<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(exception: Exception?) = Error<T>(exception)
    }

    inline fun onSuccess(block: (T) -> Unit): FirebaseResponse<T> = apply {
        if (this is Success)
            block(data)
    }

    inline fun onFailure(block: (Exception?) -> Unit): FirebaseResponse<T> = apply {
        if (this is Error)
            block(exception)
    }
}