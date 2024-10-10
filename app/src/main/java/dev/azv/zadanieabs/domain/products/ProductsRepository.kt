package dev.azv.zadanieabs.domain.products

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.azv.zadanieabs.data.database.FirebaseCallback
import dev.azv.zadanieabs.data.database.FirebaseResponse
import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.common.Globals

class ProductsRepository (
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val productRef: DatabaseReference = rootRef.child(Globals.PRODUCTS_REF)
) {
    fun getAllProducts(callback: FirebaseCallback) {
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
}