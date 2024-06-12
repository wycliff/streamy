package com.movies.streamy.view.visits


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.R
import com.movies.streamy.databinding.RowVisitItemBinding
import com.movies.streamy.model.dataSource.network.data.response.VisitsItem
import com.movies.streamy.utils.DateTimeUtils.Companion.dateToUserReadableString

class VisitsAdapter(private val clicked: (Transactions: VisitsItem) -> Unit) :
    ListAdapter<VisitsItem, VisitsAdapter.TransactionsViewHolder>(
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
            RowVisitItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class TransactionsViewHolder(
        private val binding: RowVisitItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: VisitsItem?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }
                it.tvVisitorName.text = "Visitor: "+ data?.visitorName
                it.tvVisitorPhone.text = data?.visitorPhone
                it.tvTenantName.text = "Host: "+ data?.tenant?.name
                it.tvHouseNumber.text = "House: "+ data?.tenant?.unitName
                it.tvTimeIn.text = data?.signInTime?.let { it1 ->
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

    private class TransactionsDiffCallback : DiffUtil.ItemCallback<VisitsItem>() {
        override fun areItemsTheSame(
            oldItem: VisitsItem,
            newItem: VisitsItem,
        ): Boolean {
            return oldItem.visitId == newItem.visitId
        }

        override fun areContentsTheSame(
            oldItem: VisitsItem,
            newItem: VisitsItem,
        ): Boolean {
            return oldItem == newItem
        }
    }
}