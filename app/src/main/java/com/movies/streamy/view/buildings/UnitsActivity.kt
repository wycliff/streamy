package com.movies.streamy.view.buildings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movies.streamy.R
import com.movies.streamy.databinding.ActivityUnitsBinding
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.utils.Constants
import com.movies.streamy.utils.Constants.Companion.KEY_BUILDING_ID
import com.movies.streamy.utils.Constants.Companion.KEY_TENANT_ID
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import com.movies.streamy.view.tickets.AddTicketFragment
import com.movies.streamy.view.visits.AddVisitFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnitsActivity : AppCompatActivity() {

    private var _binding: ActivityUnitsBinding? = null

    //Readable property
    private val binding get() = _binding!!

    private var buildingId: String? = null

    private lateinit var viewModel: ActionsViewModel

    private val unitsAdapter = UnitsAdapter { data: UnitEntity -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ActionsViewModel::class.java]
        _binding = ActivityUnitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtras()

        initViews()
    }

    private fun initViews() {
        buildingId?.let { viewModel.getUnits(it) }
        setUpObservers()
        setUpAdapter()

        binding.ivBtnBack.setOnClickListener {
            finish()
        }
    }

    private fun getIntentExtras() {
        buildingId = intent.getStringExtra(KEY_BUILDING_ID)
    }

    private fun setUpObservers() {
        observe(viewModel.units, ::setUpRecyclerView)
//        observe(viewModel.viewState, ::onViewStateChanged)
    }

    private fun setUpAdapter() {
        binding.rvUnits.apply {
            layoutManager = GridLayoutManager(this@UnitsActivity, 3)
            adapter?.setHasStableIds(true)
            adapter = unitsAdapter
        }
    }

    private fun setUpRecyclerView(unitsList: List<UnitEntity>) {
        if (unitsList.isEmpty()) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            unitsAdapter.submitList(unitsList)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

    private fun itemClicked(data: UnitEntity) {
        showUnitDetails(data)
    }

    private fun showUnitDetails(data: UnitEntity) {
        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog_Update_App
        )
        builder.setTitle(
            String.format(
                resources.getString(R.string.unit_details_title),
                data.unitName
            )
        )
        builder.setCancelable(true)

        if (data.ocupancyStatus.equals("Occupied")) {
            builder.setMessage(
                String.format(
                    resources.getString(R.string.unit_details),
                    data.tenantName,
                    data.tenantPhone
                )
            )
            builder.setPositiveButton(
                resources.getString(R.string.sign_in_visitor)
            ) { _: DialogInterface?, _: Int ->
                showAddVisitorBottomDialog(data.tenantId)
            }
            builder.setNegativeButton(
                resources.getString(R.string.raise_ticket)
            ) { _: DialogInterface?, _: Int ->
                showAddTicketBottomDialog()
            }

        } else {
            builder.setMessage(
                String.format(
                    resources.getString(R.string.unit_vacant),
                    data.tenantName,
                    data.tenantPhone
                )
            )
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showAddTicketBottomDialog() {
        val addTicketFragment = AddTicketFragment()

        addTicketFragment.show(
            this.supportFragmentManager, AddVisitFragment.TAG
        )
    }

    private fun showAddVisitorBottomDialog(tenantId: String?) {
        val addVisitorFragment = AddVisitFragment()

        val bundle = Bundle()
        bundle.putBoolean(Constants.KEY_IS_UNIT_REQUEST, true)
        bundle.putString(KEY_TENANT_ID, tenantId)
        addVisitorFragment.arguments = bundle

        addVisitorFragment.show(
            this.supportFragmentManager, AddVisitFragment.TAG
        )
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvUnits.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvUnits.visibility = View.VISIBLE
    }

    private fun showSnackBar(message: String?, isSuccess: Boolean = true) {
        binding.root.let {
            this.snackbar(
                it,
                message,
                if (isSuccess) R.color.md_info_color else R.color.md_error_color
            )
        }
    }
}