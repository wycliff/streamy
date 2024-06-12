package com.movies.streamy.view.comments


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.R
import com.movies.streamy.databinding.RowCommentBinding
import com.movies.streamy.model.dataSource.network.data.response.Comment
import com.movies.streamy.utils.DateTimeUtils.Companion.dateToUserReadableString

class CommentsAdapter(private val clicked: (Comments: Comment) -> Unit) :
    ListAdapter<Comment, CommentsAdapter.TransactionsViewHolder>(
        TransactionsDiffCallback()
    ) {

    private lateinit var context: Context
    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        context = parent.context
        return TransactionsViewHolder(
            RowCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class TransactionsViewHolder(
        private val binding: RowCommentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Comment?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }
                it.tvName.text = data?.name.toString()
                it.tvComment.text = data?.comment
                it.tvTime.text = data?.dateTime?.let { it1 ->
                    dateToUserReadableString(
                        it1
                    )
                }
                it.tvName.text = buildString {
                    append(data?.name)
                    append("-")
                    append(data?.userType)
                }


                it.imageItem.setBackgroundResource(R.drawable.bg_round_image)
                it.imageItem.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.Approved
                    )
                )


            }
        }
    }

    private class TransactionsDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(
            oldItem: Comment,
            newItem: Comment,
        ): Boolean {
            return oldItem.ticketCommentId == newItem.ticketCommentId
        }

        override fun areContentsTheSame(
            oldItem: Comment,
            newItem: Comment,
        ): Boolean {
            return oldItem == newItem
        }
    }
}