package com.movies.streamy.view.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentSearchBinding
import com.movies.streamy.model.dataSource.network.data.request.ParkCheckRequest
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.utils.DateTimeUtils
import com.movies.streamy.utils.getMyText
import com.movies.streamy.utils.observe
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BottomSheetDialogFragment() {

    //View binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    // viewModel
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding?.btAddTicket?.setOnClickListener {
            if (validateForm()) {
                binding?.noImageOrder?.visibility = View.GONE
                binding?.tvNoTextOrder?.visibility = View.GONE
                val parkRequest = ParkCheckRequest(
                    numberPlate = binding?.tlVehicleReg?.getMyText()
                )
                viewModel.searchVehicle(parkRequest)
            }
        }

        setUpObservers()
    }

    private fun validateForm(): Boolean {
        if (binding?.etRegNo?.text.isNullOrEmpty()) {
            binding?.etRegNo?.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        return true
    }

    private fun setUpObservers() {
        observe(viewModel.searchViewState, ::onViewState)
        observe(viewModel.vehicle, ::onVehicle)
    }

    private fun onViewState(state: SearchViewState) {
        when (state) {
            is SearchViewState.Loading -> {
                binding?.loading?.visibility = View.VISIBLE
                binding?.btnText?.visibility = View.GONE
            }

            is SearchViewState.Success -> {
                binding?.loading?.visibility = View.GONE
                binding?.btnText?.visibility = View.VISIBLE
            }

            is SearchViewState.Error -> {
                binding?.loading?.visibility = View.GONE
                binding?.btnText?.visibility = View.VISIBLE
                binding?.root?.let {
                    ViewUtils.showSnackBar(
                        state.errorMessage, false, requireContext(), it
                    )
                    binding?.noImageOrder?.visibility = View.VISIBLE
                    binding?.tvNoTextOrder?.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun onVehicle(data: ParkingObject?) {
        data?.let {

            if (data.status == false) {
                binding?.cvDetails?.visibility = View.GONE
                binding?.noImageOrder?.visibility = View.VISIBLE
                binding?.tvNoTextOrder?.visibility = View.VISIBLE
            } else {
                binding?.cvDetails?.visibility = View.VISIBLE
                binding?.noImageOrder?.visibility = View.GONE
                binding?.tvNoTextOrder?.visibility = View.GONE
            }

            //populate search found UI
            binding?.tvPlate?.text = it.numberPlate

            if (it.parkingPaid == true) {
                binding?.tvAmountStatus?.setTextColor(requireContext().getColor(R.color.Approved))
                binding?.tvAmountStatus?.text = requireContext().getText(R.string.text_paid)
            } else {
                binding?.tvAmountStatus?.setTextColor(requireContext().getColor(R.color.Rejected))
                binding?.tvAmountStatus?.text =
                    requireContext().getText(R.string.text_pending_amount)
            }

            it.balance?.let { amount ->
                if (amount > 0) binding?.tvBalance?.text = "${data.currency} $amount"
                else binding?.tvBalance?.text = "0"
            }

            it.amountPaid?.let { amount ->
                if (amount > 0) binding?.tvAmountPaid?.text = "${data.currency} $amount"
                else binding?.tvAmountPaid?.text = "0"
            }

            it.amountDue?.let { amount ->
                if (amount > 0) binding?.tvAmountDue?.text = "${data.currency} $amount"
                else binding?.tvAmountDue?.text = "0"
            }


            binding?.tvTimeIn?.text = it?.timeIn?.let { it1 ->
                DateTimeUtils.dateToUserReadableString(
                    it1
                )
            }

            binding?.tvTimePaid?.text = it?.timePaid?.let { it1 ->
                DateTimeUtils.dateToUserReadableString(
                    it1
                )
            }

            it.timeToLeave?.let { it1 ->
                binding?.tvTimeOut?.text = DateTimeUtils.dateToUserReadableString(
                    it1
                )
            }

            binding?.tvTimeSpent?.text = it.readableTimeSpent

            binding?.imageItem?.setBackgroundResource(R.drawable.bg_round_image)
            binding?.imageItem?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.Approved
                )
            )
        }
    }

    companion object {

        const val TAG = "SearchFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = SearchFragment().apply {}
    }
}