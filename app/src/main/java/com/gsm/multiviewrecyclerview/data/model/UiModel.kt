package com.gsm.multiviewrecyclerview.data.model

import com.gsm.multiviewrecyclerview.data.model.products.Product

sealed class UiModel {
    data class Banner(val list: List<Product>) : UiModel()
    data class Categories(val list: List<Category>) : UiModel()
    data class Products(val list: List<Product>) : UiModel()
}