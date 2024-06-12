package com.movies.streamy.view.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentAddTicketBinding
import com.movies.streamy.model.dataSource.network.data.request.TicketRequest
import com.movies.streamy.utils.getMyText
import com.movies.streamy.utils.observe
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.utils.LoadingDotsBounce
import com.movies.streamy.view.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTicketFragment : BottomSheetDialogFragment() {

    //View binding
    private var _binding: FragmentAddTicketBinding? = null
    private val binding get() = _binding

    // viewModel
    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var securityGuardId: String? = null
    private var loading: LoadingDotsBounce? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTicketBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        viewModel.currentUser.observe(requireActivity()) { currentUser ->
            securityGuardId = currentUser?.security_guard_id
        }

        binding?.btAddTicket?.setOnClickListener {
            if (validateForm()) {
                val ticketRequest = TicketRequest(
                    securityGuardId = securityGuardId,
                    ticketDescription = binding?.tlIssueDescription?.getMyText()
                )
                viewModel.addTicket(ticketRequest)
            }
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        observe(viewModel.viewState, ::onViewState)
    }

    private fun validateForm(): Boolean {
        if (binding?.etIssue?.text.isNullOrEmpty()) {
            binding?.etIssue?.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        return true
    }

    private fun onViewState(state: HomeViewState) {
        when (state) {
            is HomeViewState.Loading -> {
                binding?.loading?.visibility = View.VISIBLE
                binding?.btnText?.visibility = View.GONE
            }

            is HomeViewState.Success -> {
                binding?.loading?.visibility = View.VISIBLE
                binding?.btnText?.visibility = View.GONE
                binding?.root?.let {
                    ViewUtils.showSnackBar(
                        "Successfully added ticket",
                        true,
                        requireContext(),
                        it
                    )
                }
                dismiss()
            }

            is HomeViewState.Error -> {
                binding?.loading?.visibility = View.GONE
                binding?.btnText?.visibility = View.VISIBLE
                binding?.root?.let {
                    ViewUtils.showSnackBar(
                        state.errorMessage,
                        false,
                        requireContext(),
                        it
                    )
                }
            }

            else -> {}

        }
    }

    companion object {
        const val TAG = "AddVisitFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTicketFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}