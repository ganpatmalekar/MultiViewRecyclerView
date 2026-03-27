package com.gsm.multiviewrecyclerview.ui

import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.gsm.multiviewrecyclerview.R
import com.gsm.multiviewrecyclerview.data.model.products.Product
import com.gsm.multiviewrecyclerview.databinding.ActivityProductDetailsBinding
import com.gsm.multiviewrecyclerview.ui.adapter.UserReviewAdapter
import com.gsm.multiviewrecyclerview.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    @Inject
    lateinit var userReviewAdapter: UserReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            try {
                val productData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(AppConstants.PRODUCT_DATA, Product::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(AppConstants.PRODUCT_DATA)
                }

                productData?.let {
                    tvDiscountPercentage.text =
                        getString(R.string.off_percentage, it.discountPercentage)
                    Glide.with(ivProduct.context)
                        .load(it.images[0])
                        .into(ivProduct)

                    tvBrand.text = if (!it.brand.isNullOrBlank()) it.brand else "NA"
                    tvProductTitle.text = it.title
                    tvCategory.text =
                        it.category.replaceFirstChar { category -> category.uppercaseChar() }
                    tvRating.text = it.rating.toString()
                    tvReviewCount.text = getString(R.string.review_count, it.reviews.size)
                    tvStockAvailability.text = it.availabilityStatus

                    val discountedPrice = it.price * it.discountPercentage / 100
                    val finalProductPrice = it.price - discountedPrice
                    val formattedProductPrice = "%.2f".format(finalProductPrice)
                    tvDiscountedPrice.text =
                        getString(R.string.discounted_price, formattedProductPrice)

                    tvPrice.text = getString(R.string.original_price, it.price)
                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    tvDescription.text = it.description

                    tvShippingSubtext.text = it.shippingInformation
                    tvReturnSubtext.text = it.returnPolicy
                    tvWarrantySubtext.text = it.warrantyInformation
                    tvStockSubtext.text = getString(R.string.units_available, it.stock)

                    tvSkuValue.text = it.sku
                    tvWeightValue.text = getString(R.string.weight_value, it.weight)
                    tvMinOrderQtyValue.text = getString(R.string.units, it.minimumOrderQuantity)

                    cgTags.removeAllViews()
                    it.tags.forEach { tag ->
                        val chip = Chip(this@ProductDetailsActivity).apply {
                            text = tag.replaceFirstChar { name -> name.uppercaseChar() }
                            isCheckable = false
                            isClickable = false
                        }
                        cgTags.addView(chip)
                    }

                    tvReviews.text = getString(R.string.total_reviews, it.reviews.size)
                    rvReviews.setHasFixedSize(true)
                    rvReviews.addItemDecoration(
                        DividerItemDecoration(this@ProductDetailsActivity, RecyclerView.VERTICAL)
                    )
                    rvReviews.adapter = userReviewAdapter
                    if (it.reviews.isNotEmpty()) {
                        userReviewAdapter.submitList(it.reviews)
                    } else {
                        userReviewAdapter.submitList(emptyList())
                    }

                    toolbarLayout.tvToolbarTitle.text = it.title
                    toolbarLayout.ivBack.visibility = View.VISIBLE
                    toolbarLayout.ivBack.setOnClickListener {
                        finish()
                    }
                }
            } catch (e: Exception) {
                Log.e("ProductDetailsActivity", "setupUI: Error => ${e.localizedMessage}")
            }
        }
    }
}