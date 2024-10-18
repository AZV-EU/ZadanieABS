package dev.azv.zadanieabs.domain.model.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.azv.zadanieabs.R
import dev.azv.zadanieabs.common.Constants
import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.data.product.ProductsRepository
import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.di.RemoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor (
    private val sessionManager: SessionManager,
    @RemoteRepository private val repository: ProductsRepository
): ViewModel() {
    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isSyncing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _error: MutableStateFlow<Int> = MutableStateFlow(0)
    val error: StateFlow<Int> = _error

    private var syncJob: Job? = null
    val canSync: Boolean = syncJob?.isActive != true

    init {
        checkUserLogin()
        syncProducts()
    }

    fun checkUserLogin(): Boolean {
        _isLoggedIn.value = sessionManager.getUser() != null
        return isLoggedIn.value
    }

    fun syncProducts() {
        //if (!checkUserLogin()) return
        if (!canSync) return

        _isSyncing.value = true
        _error.update { 0 }

        syncJob = viewModelScope.launch {
            repository.getAllProducts()
                .collect { response ->
                    response.onSuccess { products ->
                        _products.update { products }
                    }.onFailure { exception ->
                        _products.update { emptyList() }
                        _error.update { R.string.sync_products_error }
                        Log.e(Constants.LOG_TAG, exception?.stackTraceToString() ?: "Unknown ProductsRepository.getAllProducts() error")
                    }
                }
            _isSyncing.update { false }
        }
    }

    private fun logout() {
        sessionManager.setUser(null)
        syncJob?.cancel()
        _isLoggedIn.value = false
    }

    private fun showLoginScreen() {

    }
}