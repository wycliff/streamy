package com.movies.streamy.view.visits

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movies.streamy.R
import com.movies.streamy.databinding.ActivityVisitsBinding
import com.movies.streamy.model.dataSource.network.data.response.SignOutResponse
import com.movies.streamy.model.dataSource.network.data.response.VisitsItem
import com.movies.streamy.utils.Constants.Companion.KEY_IS_UNIT_REQUEST
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.utils.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.String.format


@AndroidEntryPoint
class VisitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisitsBinding
    var progress = ProgressDialog()

    // viewModel
    private lateinit var viewModel: HomeViewModel
    private var securityGuardId: String? = null
    private var commentEditTextInput: String? = null


    private val visitsAdapter =
        VisitsAdapter { data: VisitsItem -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = ActivityVisitsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        showShimmerEffect()
        getCurrentUser()


        binding.addButton.setOnClickListener {
            showAddVisitorBottomDialog()
        }

        binding.ivBtnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }
        setUpObservers()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getVisits(securityGuardId)

            object : CountDownTimer(3000, 1000) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    binding.swipeRefresh.isRefreshing = false
                }
            }.start()
        }
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.rvVisits.apply {
            layoutManager =
                LinearLayoutManager(this@VisitsActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = visitsAdapter
        }
    }

    private fun showAddVisitorBottomDialog() {
        val addVisitorFragment = AddVisitFragment()
        val bundle = Bundle()
        bundle.putBoolean(KEY_IS_UNIT_REQUEST, false)
//        bundle.putString(KEY_TENANT_ID, )
        addVisitorFragment.arguments = bundle

        addVisitorFragment.show(
            this.supportFragmentManager, AddVisitFragment.TAG
        )
    }

    private fun getCurrentUser() {
        lifecycleScope.launch {
            viewModel.currentUser.observe(this@VisitsActivity) { currentUser ->
                securityGuardId = currentUser?.security_guard_id
                securityGuardId?.let {
                    //get running balance
                    viewModel.getVisits(securityGuardId)
                }
            }
        }
    }

    private fun setUpObservers() {
        observe(viewModel.visits, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewStateChanged)
        observe(viewModel.signOut, ::onSignOutVisitor)
    }

    private fun setUpRecyclerView(visitsList: List<VisitsItem>?) {
        if (visitsList?.isEmpty() == true) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            visitsAdapter.submitList(visitsList)
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


    private fun onSignOutVisitor(signedOutResponse: SignOutResponse) {
        progress.cancel()
        showSnackBar(signedOutResponse.message, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvVisits.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvVisits.visibility = View.VISIBLE
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

    private fun itemClicked(data: VisitsItem) {
        signOutVisitor(data)
    }

    private fun signOutVisitor(data: VisitsItem) {
        //Container
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL

        //Edit text
        val editTextField = EditText(this@VisitsActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(80, 4, 40, 4)
        editTextField.layoutParams = lp
        editTextField.layoutParams = lp
        editTextField.gravity = Gravity.TOP or Gravity.LEFT
        editTextField.maxLines = 1

        //Add et to container
        container.addView(editTextField, lp)


        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog_Update_App
        )
        builder.setCancelable(false)
        builder.setTitle(resources.getString(R.string.signout_title))
        builder.setMessage(format(getString(R.string.signout_visitor_message),data.visitorName, data.tenant?.unitName))
        builder.setView(container)
        builder.setPositiveButton(
            resources.getString(R.string.proceed)
        ) { _: DialogInterface?, _: Int ->
            commentEditTextInput = editTextField.text.toString()
            viewModel.signOutVisitor(securityGuardId, data.visitId, commentEditTextInput)
            progress.show(this@VisitsActivity)
        }
        builder.setNegativeButton(
            resources.getString(R.string.cancel)
        ) { _: DialogInterface?, _: Int ->

        }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}