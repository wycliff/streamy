package com.movies.streamy.view.buildings


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.streamy.databinding.RowBuildingBinding
import com.movies.streamy.model.dataSource.local.table.BuildingEntity


class BuildingsAdapter(private val clicked: (building: BuildingEntity) -> Unit) :
    ListAdapter<BuildingEntity, BuildingsAdapter.BuildingsViewHolder>(
        BuildingsDiffCallback()
    ) {

    private lateinit var context: Context
    override fun onBindViewHolder(holder: BuildingsViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingsViewHolder {
        context = parent.context
        return BuildingsViewHolder(
            RowBuildingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class BuildingsViewHolder(
        private val binding: RowBuildingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BuildingEntity?) {

            binding.let {
                it.root.setOnClickListener {
                    if (data != null) {
                        clicked.invoke(data)
                    }
                }
                it.tvBuildingName.text = data?.buildingName
            }
        }
    }

    private class BuildingsDiffCallback : DiffUtil.ItemCallback<BuildingEntity>() {
        override fun areItemsTheSame(
            oldItem: BuildingEntity,
            newItem: BuildingEntity,
        ): Boolean {
            return oldItem.buildingId == newItem.buildingId
        }

        override fun areContentsTheSame(
            oldItem: BuildingEntity,
            newItem: BuildingEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }
}