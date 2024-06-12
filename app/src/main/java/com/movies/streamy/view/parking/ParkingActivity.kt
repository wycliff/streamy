package com.movies.streamy.view.parking

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.streamy.R
import com.movies.streamy.databinding.ActivityParkingBinding
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.visits.AddVisitFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkingBinding

    // viewModel
    private lateinit var viewModel: HomeViewModel
    private var userId: Int? = null

    private var securityGuardId: String? = null

    private val parkingAdapter =
        ParkingAdapter { data: ParkingObject -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = ActivityParkingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        showShimmerEffect()
        getCurrentUser()

        binding.ivBtnSearch.setOnClickListener {
            showSearchVehicleBottomDialog()
        }

        binding.addButton.setOnClickListener {
            showAddVehicleBottomDialog()
        }

        binding.ivBtnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }
        setUpObservers()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getParkingList(securityGuardId)

            object : CountDownTimer(3000, 1000) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    binding.swipeRefresh.isRefreshing = false
                }
            }.start()
        }
        setUpAdapter()
    }

    private fun getCurrentUser() {
        lifecycleScope.launch {
            viewModel.currentUser.observe(this@ParkingActivity) { currentUser ->
                securityGuardId = currentUser?.security_guard_id
                securityGuardId?.let {
                    //get running balance
                    viewModel.getParkingList(securityGuardId)
                }
            }
        }
    }

    private fun setUpObservers() {
        observe(viewModel.parkingList, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewStateChanged)
        observe(viewModel.isAdded, ::onVehicleAdded)
    }

    private fun setUpAdapter() {
        binding.rvParking.apply {
            layoutManager =
                LinearLayoutManager(this@ParkingActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = parkingAdapter
        }
    }

    private fun setUpRecyclerView(ticketsList: List<ParkingObject?>?) {
        if (ticketsList?.isEmpty() == true) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            parkingAdapter.submitList(ticketsList)
        }
    }

    private fun onViewStateChanged(state: HomeViewState) {
        hideShimmerEffect()
        when (state) {
            is HomeViewState.Loading -> {
                showShimmerEffect()
                binding.noTextOrder.visibility = View.GONE
                binding.noImageOrder.visibility = View.GONE
            }

            is HomeViewState.Success -> {
                hideShimmerEffect()
                binding.noTextOrder.visibility = View.GONE
                binding.noImageOrder.visibility = View.GONE
            }

            is HomeViewState.Error -> {
                hideShimmerEffect()
                showSnackBar(state.errorMessage, false)
                binding.noTextOrder.visibility = View.VISIBLE
                binding.noImageOrder.visibility = View.VISIBLE
            }

            else -> {}
        }
    }

    private fun onVehicleAdded(isAdded: Boolean) {
        Log.e("","_HERE3")
        viewModel.getParkingList(securityGuardId)

        binding.swipeRefresh.isRefreshing = true
        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                binding.swipeRefresh.isRefreshing = false
            }
        }.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
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

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvParking.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvParking.visibility = View.VISIBLE
    }

    private fun itemClicked(data: ParkingObject) {
        //to-do
    }

    private fun showAddVehicleBottomDialog() {
        val addVehicleFragment = ParkingFragment()

        addVehicleFragment.show(
            this.supportFragmentManager, AddVisitFragment.TAG
        )
    }

    private fun showSearchVehicleBottomDialog() {
        val searchVehicleFragment = SearchFragment()

        searchVehicleFragment.show(
            this.supportFragmentManager, SearchFragment.TAG
        )
    }
}