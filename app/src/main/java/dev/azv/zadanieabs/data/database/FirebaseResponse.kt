package dev.azv.zadanieabs.data.database

import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.data.user.User

data class FirebaseResponse(
    var products: List<Product>? = null,
    var user: User? = null,
    var exception: Exception? = null
)