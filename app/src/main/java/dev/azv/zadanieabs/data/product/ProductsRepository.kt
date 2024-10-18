package dev.azv.zadanieabs.data.product

import dev.azv.zadanieabs.data.database.FirebaseResponse
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getAllProducts(): Flow<FirebaseResponse<List<Product>>>
}