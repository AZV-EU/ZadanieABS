package dev.azv.zadanieabs.domain.products

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.azv.zadanieabs.data.database.FirebaseCallback
import dev.azv.zadanieabs.data.database.FirebaseResponse
import dev.azv.zadanieabs.data.products.Product
import dev.azv.zadanieabs.domain.common.Globals.PRODUCTS_REF
import kotlinx.coroutines.tasks.await

class ProductsRepository (
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val productRef: DatabaseReference = rootRef.child(PRODUCTS_REF)
) {
    fun getResponseFromDatabaseCallback(callback: FirebaseCallback) {
        productRef.get().addOnCompleteListener { task ->
            val response = FirebaseResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.products = result.children.map { snapshot ->
                        snapshot.getValue(Product::class.java)!!
                    }
                }
            } else
                response.exception = task.exception
            callback.onResponse(response)
        }
    }

    fun getResponseFromDatabaseLiveData() : MutableLiveData<FirebaseResponse> {
        val mutableLiveData = MutableLiveData<FirebaseResponse>()
        productRef.get().addOnCompleteListener { task ->
            val response = FirebaseResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.products = result.children.map { snapshot ->
                        snapshot.getValue(Product::class.java)!!
                    }
                }
            } else
                response.exception = task.exception
            mutableLiveData.value = response
        }
        return mutableLiveData
    }

    suspend fun getResponseFromDatabaseCoroutine(): FirebaseResponse {
        val response = FirebaseResponse()
        try {
            response.products = productRef.get().await().children.map { snapshot ->
                snapshot.getValue(Product::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }
}