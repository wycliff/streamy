package com.movies.streamy.view.parking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.R
import com.movies.streamy.databinding.RowParkingBinding
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.utils.DateTimeUtils


class ParkingAdapter(private val clicked: (ParkingItems: ParkingObject) -> Unit) :
    ListAdapter<ParkingObject, ParkingAdapter.ParkingViewHolder>(
        ParkingDiffCallback()
    ) {

    private lateinit var context: Context
    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        context = parent.context
        return ParkingViewHolder(
            RowParkingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class ParkingViewHolder(
        private val binding: RowParkingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ParkingObject?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }

                it.tvPlate.text = data?.numberPlate

                if (data?.paidStatus == "0") {
                    it.tvAmountStatus.setTextColor(context.getColor(R.color.Rejected))
                    it.tvAmountStatus.text = context.getText(R.string.text_pending_amount)
                } else {
                    it.tvAmountStatus.setTextColor(context.getColor(R.color.Approved))
                    it.tvAmountStatus.text = context.getText(R.string.text_paid)
                }


                data?.amountDue?.let { amount ->
                    if (amount > 0)
                        it.tvAmountDue.text = "${data.currency} $amount"
                    else
                        it.tvAmountDue.text = "0"
                }

                data?.amountPaid?.let { amount ->
                    if (amount > 0)
                        it.tvAmountPaid.text = "${data.currency} $amount"
                    else
                        it.tvAmountPaid.text = "0"
                }

                data?.balance?.let { amount ->
                    if (amount > 0)
                        it.tvBalance.text = "${data.currency} $amount"
                    else
                        it.tvBalance.text = "0"
                }

                it.tvTimePaid.text = data?.timePaid?.let { it1 ->
                    DateTimeUtils.dateToUserReadableString(
                        it1
                    )
                }

                it.tvTimeIn.text = data?.timeIn?.let { it1 ->
                    DateTimeUtils.dateToUserReadableString(
                        it1
                    )
                }

                it.tvTimeOut.text = data?.timeToLeave?.let { it1 ->
                    DateTimeUtils.dateToUserReadableString(
                        it1
                    )
                }

                it.tvTimeSpent.text = data?.readableTimeSpent


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

    private class ParkingDiffCallback : DiffUtil.ItemCallback<ParkingObject>() {
        override fun areItemsTheSame(
            oldItem: ParkingObject,
            newItem: ParkingObject,
        ): Boolean {
            return oldItem.parkId == newItem.parkId
        }

        override fun areContentsTheSame(
            oldItem: ParkingObject,
            newItem: ParkingObject,
        ): Boolean {
            return oldItem == newItem
        }
    }
}