package com.gsm.multiviewrecyclerview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.multiviewrecyclerview.R
import com.gsm.multiviewrecyclerview.data.model.products.Review
import com.gsm.multiviewrecyclerview.databinding.ItemReviewsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserReviewAdapter : ListAdapter<Review, UserReviewAdapter.UserReviewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(
                old: Review,
                new: Review
            ): Boolean = old.date == new.date

            override fun areContentsTheSame(
                old: Review,
                new: Review
            ): Boolean = old == new
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): UserReviewHolder {
        return UserReviewHolder(
            ItemReviewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: UserReviewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class UserReviewHolder(val binding: ItemReviewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.apply {
                tvReviewerName.text = review.reviewerName
                tvReviewComment.text = review.comment
                tvReviewDate.text = formatDate(review.date)
                tvInitial.text = review.reviewerName.first().toString()

                val stars = listOf(ivStar1, ivStar2, ivStar3, ivStar4, ivStar5)
                stars.forEachIndexed { index, imageView ->
                    if (index < review.rating) {
                        imageView.setImageResource(R.drawable.ic_rating)
                    } else {
                        imageView.setImageResource(R.drawable.ic_rate)
                    }
                }
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                // Input pattern as per the API format
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateString)
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (_: Exception) {
                dateString
            }
        }
    }
}