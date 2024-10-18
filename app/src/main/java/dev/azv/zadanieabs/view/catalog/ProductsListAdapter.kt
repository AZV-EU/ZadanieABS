package dev.azv.zadanieabs.view.catalog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.azv.zadanieabs.R
import dev.azv.zadanieabs.data.product.Product
import dev.azv.zadanieabs.databinding.ItemProductBinding

class ProductsListAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductsListAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ProductViewHolder(
        itemView: View,
        private val binding: ItemProductBinding = ItemProductBinding.bind(itemView)
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) = with(itemView) {
            binding.textProductName.text = product.name
            binding.textProductDescription.text = product.shortDescription

            setOnClickListener { onProductClick(product) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        }
    }
}