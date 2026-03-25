package com.gsm.multiviewrecyclerview.data.model.products

data class ProductResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)