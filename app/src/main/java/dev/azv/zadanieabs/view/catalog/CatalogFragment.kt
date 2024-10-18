package dev.azv.zadanieabs.view.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.azv.zadanieabs.common.Constants
import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.databinding.FragmentCatalogBinding
import dev.azv.zadanieabs.domain.model.catalog.CatalogViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment: Fragment() {
    private lateinit var binding: FragmentCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val productsListAdapter: ProductsListAdapter = ProductsListAdapter(::onProductClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshProducts.setOnRefreshListener {
            binding.swipeRefreshProducts.isRefreshing = false
            viewModel.syncProducts()
        }

        binding.productsRecyclerView.adapter = productsListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSyncing.collect {
                binding.swipeRefreshProducts.isRefreshing = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect {
                productsListAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                if (it != 0) {
                    Log.i(Constants.LOG_TAG,"Error: ${getString(it)}")
                }
            }
        }
    }

    private fun onProductClicked(product: Product) {
        print("Selected product id: ${product.id}")
    }
}