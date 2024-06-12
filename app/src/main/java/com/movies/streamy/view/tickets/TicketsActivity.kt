package com.movies.streamy.view.tickets

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movies.streamy.R
import com.movies.streamy.databinding.ActivityTicketsBinding
import com.movies.streamy.model.dataSource.network.data.response.TicketsItem
import com.movies.streamy.utils.Constants.Companion.KEY_TICKET_ID
import com.movies.streamy.utils.Constants.Companion.KEY_TICKET_MESSAGE
import com.movies.streamy.utils.Constants.Companion.KEY_TICKET_TIME
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import com.movies.streamy.view.comments.CommentsActivity
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.visits.AddVisitFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketsBinding

    // viewModel
    private lateinit var viewModel: HomeViewModel
    private var userId: Int? = null

    private var securityGuardId: String? = null

    private val ticketsAdapter =
        TicketsAdapter { data: TicketsItem -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = ActivityTicketsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        showShimmerEffect()
        getCurrentUser()

        binding.addButton.setOnClickListener {
            showAddTicketBottomDialog()
        }

        binding.ivBtnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }
        setUpObservers()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getTickets(securityGuardId)

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
            viewModel.currentUser.observe(this@TicketsActivity) { currentUser ->
                securityGuardId = currentUser?.security_guard_id
                securityGuardId?.let {
                    //get running balance
                    viewModel.getTickets(securityGuardId)
                }
            }
        }
    }

    private fun setUpObservers() {
        observe(viewModel.tickets, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewStateChanged)
    }

    private fun setUpAdapter() {
        binding.rvTickets.apply {
            layoutManager =
                LinearLayoutManager(this@TicketsActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = ticketsAdapter
        }
    }

    private fun setUpRecyclerView(ticketsList: List<TicketsItem?>?) {
        if (ticketsList?.isEmpty() == true) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            ticketsAdapter.submitList(ticketsList)
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
        binding.rvTickets.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvTickets.visibility = View.VISIBLE
    }

    private fun itemClicked(data: TicketsItem) {
        val intent = Intent(this@TicketsActivity, CommentsActivity::class.java)
        intent.putExtra( KEY_TICKET_ID, data.ticketId)
        intent.putExtra( KEY_TICKET_MESSAGE, data.ticketDescription)
        intent.putExtra( KEY_TICKET_TIME, data.dateTime)
        startActivity(intent)
    }

    private fun viewComment(comment: String?) {
        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog_Update_App
        )
        builder.setCancelable(false)
        builder.setMessage(comment)
        builder.setPositiveButton(
            resources.getString(R.string.ok)
        ) { _: DialogInterface?, _: Int ->

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
}