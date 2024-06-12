package com.movies.streamy.view.comments

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
import com.movies.streamy.databinding.ActivityCommentsBinding
import com.movies.streamy.model.dataSource.network.data.request.AddCommentRequest
import com.movies.streamy.model.dataSource.network.data.response.Comment
import com.movies.streamy.utils.Constants
import com.movies.streamy.utils.Constants.Companion.KEY_TICKET_MESSAGE
import com.movies.streamy.utils.Constants.Companion.KEY_TICKET_TIME
import com.movies.streamy.utils.DateTimeUtils
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.utils.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    var progress = ProgressDialog()

    // viewModel
    private lateinit var viewModel: HomeViewModel

    private var ticketId: String? = null
    private var ticketDesc: String? = null
    private var ticketTime: String? = null

    private var securityGuardId: String? = null
    private var commentEditTextInput: String? = null

    private val commentsAdapter =
        CommentsAdapter { data: Comment -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = ActivityCommentsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        getIntentExtras()

        initViews()
    }

    private fun getIntentExtras() {
        ticketId = intent.getStringExtra(Constants.KEY_TICKET_ID)
        ticketDesc = intent.getStringExtra(KEY_TICKET_MESSAGE)
        ticketTime = intent.getStringExtra(KEY_TICKET_TIME)

        binding.tvIssueDescription.text = ticketDesc
        binding.tvTime.text = ticketTime?.let {
            DateTimeUtils.dateToUserReadableString(
                it
            )
        }

    }

    private fun initViews() {
        showShimmerEffect()
        getCurrentUser()

        viewModel.getComments(ticketId)
        binding.addButton.setOnClickListener {

        }

        binding.ivBtnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }
        setUpObservers()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getComments(ticketId)

            object : CountDownTimer(3000, 1000) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    binding.swipeRefresh.isRefreshing = false
                }
            }.start()
        }
        binding.addButton.setOnClickListener { addComment() }
        setUpObservers()
        setUpAdapter()
    }


    private fun setUpObservers() {
        observe(viewModel.comments, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewStateChanged)
    }

    private fun getCurrentUser() {
        lifecycleScope.launch {
            viewModel.currentUser.observe(this@CommentsActivity) { currentUser ->
                securityGuardId = currentUser?.security_guard_id
                securityGuardId?.let {
                    //get running balance
                    viewModel.getVisits(securityGuardId)
                }
            }
        }
    }

    private fun setUpAdapter() {
        binding.rvComments.apply {
            layoutManager =
                LinearLayoutManager(this@CommentsActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = commentsAdapter
        }
    }

    private fun setUpRecyclerView(commentList: List<Comment?>?) {
        if (commentList?.isEmpty() == true) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            commentsAdapter.submitList(commentList)
        }
    }

    private fun itemClicked(data: Comment) {

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
                progress.cancel()
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

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvComments.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvComments.visibility = View.VISIBLE
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

    private fun addComment() {
        //Container
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL

        //Edit text
        val editTextField = EditText(this@CommentsActivity)
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
        builder.setTitle(resources.getString(R.string.comment_title))
        builder.setMessage(
            java.lang.String.format(
                getString(R.string.enter_comment)
            )
        )
        builder.setView(container)
        builder.setPositiveButton(
            resources.getString(R.string.proceed)
        ) { _: DialogInterface?, _: Int ->
            commentEditTextInput = editTextField.text.toString()
            var commentRequest = AddCommentRequest(commentEditTextInput, securityGuardId, ticketId)
            viewModel.addComment(commentRequest)
            progress.show(this@CommentsActivity)
        }
        builder.setNegativeButton(
            resources.getString(R.string.cancel)
        ) { _: DialogInterface?, _: Int ->

        }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}