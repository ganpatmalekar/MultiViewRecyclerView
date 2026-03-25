package com.gsm.multiviewrecyclerview.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.gsm.multiviewrecyclerview.data.model.Category
import com.gsm.multiviewrecyclerview.data.model.UiModel
import com.gsm.multiviewrecyclerview.data.model.products.Product
import com.gsm.multiviewrecyclerview.databinding.ItemBannerListBinding
import com.gsm.multiviewrecyclerview.databinding.ItemCategoryListBinding
import com.gsm.multiviewrecyclerview.databinding.ItemProductBinding

class MainUiAdapter : ListAdapter<UiModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        const val VIEW_BANNER = 1
        const val VIEW_CATEGORY = 2
        const val VIEW_PRODUCT = 3

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(
                old: UiModel,
                new: UiModel
            ): Boolean = old == new

            override fun areContentsTheSame(
                old: UiModel,
                new: UiModel
            ): Boolean = old == new
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.Banner -> VIEW_BANNER
            is UiModel.Categories -> VIEW_CATEGORY
            is UiModel.Products -> VIEW_PRODUCT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_BANNER -> {
                BannerViewHolder(
                    ItemBannerListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            VIEW_CATEGORY -> {
                CategoryViewHolder(
                    ItemCategoryListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                ProductViewHolder(
                    ItemProductBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is UiModel.Banner -> (holder as BannerViewHolder).bind(item.list)
            is UiModel.Categories -> (holder as CategoryViewHolder).bind(item.list)
            is UiModel.Products -> (holder as ProductViewHolder).bind(item.product)
        }
    }

    class BannerViewHolder(val binding: ItemBannerListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List<Product>) {
            binding.rvBanner.apply {
                val bannerAdapter = BannerAdapter()
                setHasFixedSize(true)
                adapter = bannerAdapter
                bannerAdapter.submitList(list)
            }
        }
    }

    class CategoryViewHolder(val binding: ItemCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List<Category>) {
            binding.chipGroupCategories.removeAllViews()
            list.forEach { category ->
                val chip = Chip(binding.root.context).apply {
                    text = category.name
                    isCheckable = true
                    isClickable = true
                }
                binding.chipGroupCategories.addView(chip)
            }
        }
    }

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                tvTitle.text = product.title

                val discountedPrice = product.price * product.discountPercentage / 100
                val finalProductPrice = product.price - discountedPrice
                val formattedProductPrice = "%.2f".format(finalProductPrice)
                tvPrice.text = "$ $formattedProductPrice"
            }
        }
    }
}