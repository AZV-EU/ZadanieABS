package dev.azv.zadanieabs.domain.products

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.azv.zadanieabs.data.database.FirebaseResponse
import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.common.Constants
import dev.azv.zadanieabs.data.product.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteProductsRepository @Inject internal constructor() : ProductsRepository {
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val productsRef: DatabaseReference = rootRef.child(Constants.PRODUCTS_REF)

    override fun getAllProducts(): Flow<FirebaseResponse<List<Product>>> = flow {
        val response = productsRef.get()
        response.await()

        if (response.isSuccessful)
            emit(FirebaseResponse.success(response.result.children.map { ds -> ds.getValue(Product::class.java)!! }))
        else
            emit(FirebaseResponse.error(response.exception))
    }.catch {
        emit(FirebaseResponse.error(Exception(it)))
    }
}