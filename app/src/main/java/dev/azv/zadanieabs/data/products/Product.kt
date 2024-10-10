package dev.azv.zadanieabs.data.products

import android.media.Image

data class Product (
    var name: String? = null,
    var shortDescription: String? = null,
    var price: Int? = null,
    var image: Image? = null
)