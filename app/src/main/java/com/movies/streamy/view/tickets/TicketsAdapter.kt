package com.movies.streamy.view.tickets


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.R
import com.movies.streamy.databinding.RowTicketBinding
import com.movies.streamy.model.dataSource.network.data.response.TicketsItem
import com.movies.streamy.utils.DateTimeUtils.Companion.dateToUserReadableString

class TicketsAdapter(private val clicked: (Tickets: TicketsItem) -> Unit) :
    ListAdapter<TicketsItem, TicketsAdapter.TicketsViewHolder>(
        TicketsDiffCallback()
    ) {

    private lateinit var context: Context
    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        context = parent.context
        return TicketsViewHolder(
            RowTicketBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class TicketsViewHolder(
        private val binding: RowTicketBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TicketsItem?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }
                it.tvIssueDescription.text = data?.ticketDescription
                it.tvTimeIn.text = data?.dateTime?.let { it1 ->
                    dateToUserReadableString(
                        it1
                    )
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

    private class TicketsDiffCallback : DiffUtil.ItemCallback<TicketsItem>() {
        override fun areItemsTheSame(
            oldItem: TicketsItem,
            newItem: TicketsItem,
        ): Boolean {
            return oldItem.ticketId == newItem.ticketId
        }

        override fun areContentsTheSame(
            oldItem: TicketsItem,
            newItem: TicketsItem,
        ): Boolean {
            return oldItem == newItem
        }
    }
}