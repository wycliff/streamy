package com.movies.streamy.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.databinding.RowMovieItemBinding
import com.movies.streamy.model.dataSource.network.data.response.MovieId

class MovieIdAdapter(private val clicked: (Movies: MovieId) -> Unit) :
    ListAdapter<MovieId, MovieIdAdapter.TransactionsViewHolder>(
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
            RowMovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class TransactionsViewHolder(
        private val binding: RowMovieItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieId?) {
            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }
                it.tvId.text = data?.id
            }
        }
    }

    private class TransactionsDiffCallback : DiffUtil.ItemCallback<MovieId>() {
        override fun areItemsTheSame(
            oldItem: MovieId,
            newItem: MovieId,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieId,
            newItem: MovieId,
        ): Boolean {
            return oldItem == newItem
        }
    }
}