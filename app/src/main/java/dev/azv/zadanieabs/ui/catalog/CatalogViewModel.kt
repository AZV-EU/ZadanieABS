package dev.azv.zadanieabs.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.ViewModel
import dev.azv.zadanieabs.data.database.FirebaseCallback
import dev.azv.zadanieabs.data.database.FirebaseResponse
import dev.azv.zadanieabs.domain.products.ProductsRepository
import kotlinx.coroutines.Dispatchers

class CatalogViewModel (
    private val repository: ProductsRepository = ProductsRepository()
): ViewModel() {
    fun getResponseCallback(callback: FirebaseCallback) {
        repository.getResponseFromDatabaseCallback(callback)
    }

    fun getResponseLiveData(): LiveData<FirebaseResponse> {
        return repository.getResponseFromDatabaseLiveData()
    }

    val responseLiveDataCoroutine = liveData(Dispatchers.IO) {
        emit(repository.getResponseFromDatabaseCoroutine())
    }
}