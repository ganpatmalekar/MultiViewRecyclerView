package com.gsm.multiviewrecyclerview.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsm.multiviewrecyclerview.data.model.products.Product
import com.gsm.multiviewrecyclerview.databinding.ItemBannerBinding

class BannerAdapter : ListAdapter<Product, BannerAdapter.BannerViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                old: Product, new: Product
            ): Boolean = old.id == new.id

            override fun areContentsTheSame(
                old: Product, new: Product
            ): Boolean = old == new
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, position: Int
    ): BannerViewHolder {
        return BannerViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BannerViewHolder, position: Int
    ) {
        holder.bind(getItem(position))
    }

    class BannerViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                tvTitle.text = product.title
                tvBrand.text = product.brand
                tvRating.text = product.rating.toString()
                tvCategory.text = product.category
                tvStock.text = product.availabilityStatus

                val discountedPrice = product.price * product.discountPercentage / 100
                val finalProductPrice = product.price - discountedPrice
                val formattedProductPrice = "%.2f".format(finalProductPrice)
                tvDiscountedPrice.text = "$ $formattedProductPrice"
                tvPrice.text = "$ ${product.price}"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                Glide.with(ivBanner.context)
                    .load(product.thumbnail)
                    .into(ivBanner)
            }
        }
    }
}