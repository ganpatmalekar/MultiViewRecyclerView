package com.gsm.multiviewrecyclerview.network

import com.gsm.multiviewrecyclerview.data.model.Category
import com.gsm.multiviewrecyclerview.data.model.products.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("products")
    suspend fun getBanners(@Query("limit") limit: Int): ProductResponse

    @GET("products/categories")
    suspend fun getCategories(): List<Category>

    @GET("products")
    suspend fun getProducts(): ProductResponse
}