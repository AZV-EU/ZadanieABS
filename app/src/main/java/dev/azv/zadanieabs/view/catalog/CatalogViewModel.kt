package dev.azv.zadanieabs.view.catalog

import androidx.lifecycle.ViewModel
import dev.azv.zadanieabs.data.database.FirebaseCallback
import dev.azv.zadanieabs.domain.products.ProductsRepository

class CatalogViewModel (
    private val repository: ProductsRepository = ProductsRepository()
): ViewModel() {
    fun getResponse(callback: FirebaseCallback) {
        repository.getAllProducts(callback)
    }
}