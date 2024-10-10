package dev.azv.zadanieabs.data.database

import dev.azv.zadanieabs.data.products.Product

data class FirebaseResponse(
    var products: List<Product>? = null,
    var exception: Exception? = null
)