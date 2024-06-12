package com.movies.streamy.view.buildings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.R
import com.movies.streamy.databinding.RowUnitBinding
import com.movies.streamy.model.dataSource.local.table.UnitEntity


class UnitsAdapter(private val clicked: (Units: UnitEntity) -> Unit) :
    ListAdapter<UnitEntity, UnitsAdapter.UnitsViewHolder>(
        UnitsDiffCallback()
    ) {

    private lateinit var context: Context
    override fun onBindViewHolder(holder: UnitsViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitsViewHolder {
        context = parent.context
        return UnitsViewHolder(
            RowUnitBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class UnitsViewHolder(
        private val binding: RowUnitBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UnitEntity?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }

                if (data?.ocupancyStatus.equals("Occupied")) {
                    it.cvUnit.setBackgroundResource(R.drawable.bg_unit_white)
                } else {
                    it.cvUnit.setBackgroundResource(R.drawable.bg_unit_grey)
                }


                it.tvUnitName.text = data?.unitName
            }
        }
    }

    private class UnitsDiffCallback : DiffUtil.ItemCallback<UnitEntity>() {
        override fun areItemsTheSame(
            oldItem: UnitEntity,
            newItem: UnitEntity,
        ): Boolean {
            return oldItem.tenantId == newItem.tenantId
        }

        override fun areContentsTheSame(
            oldItem: UnitEntity,
            newItem: UnitEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}