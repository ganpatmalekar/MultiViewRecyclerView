package com.gsm.multiviewrecyclerview.data

import com.gsm.multiviewrecyclerview.data.model.UiModel
import com.gsm.multiviewrecyclerview.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val networkService: NetworkService) {

    fun getMainScreenData(): Flow<List<UiModel>> {
        return flow {
            val banners = networkService.getBanners(5).products
            val categories = networkService.getCategories()
            val products = networkService.getProducts().products

            val uiList = mutableListOf<UiModel>()

            uiList.add(UiModel.Banner(banners))
            uiList.add(UiModel.Categories(categories))
            uiList.add(UiModel.Products(products))

            emit(uiList)
        }
    }
}