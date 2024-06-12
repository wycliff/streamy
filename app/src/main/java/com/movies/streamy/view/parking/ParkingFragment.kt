package com.movies.streamy.view.parking


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentParkingBinding
import com.movies.streamy.model.dataSource.network.data.request.ParkRequest
import com.movies.streamy.model.dataSource.network.data.response.ParkVehicleResponse
import com.movies.streamy.utils.getMyText
import com.movies.streamy.utils.observe
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.utils.LoadingDotsBounce
import com.movies.streamy.view.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ParkingFragment : BottomSheetDialogFragment() {

    //View binding
    private var _binding: FragmentParkingBinding? = null
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
        _binding = FragmentParkingBinding.inflate(inflater, container, false)
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
                val parkRequest = ParkRequest(
                    numberPlate = binding?.tlVehicleReg?.getMyText(),
                    securityGuardId = securityGuardId,
                )
                viewModel.addVehicle(parkRequest)
            }
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        observe(viewModel.parkingViewState, ::onViewState)
        observe(viewModel.parkedState, ::onResult)
    }

    private fun validateForm(): Boolean {
        if (binding?.etRegNo?.text.isNullOrEmpty()) {
            binding?.etRegNo?.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        return true
    }

    private fun onViewState(state: ParkingViewState) {
        when (state) {
            is ParkingViewState.Loading -> {
                binding?.loading?.visibility = View.VISIBLE
                binding?.btnText?.visibility = View.GONE
            }

            is ParkingViewState.Success -> {
                binding?.loading?.visibility = View.GONE
                binding?.btnText?.visibility = View.VISIBLE

                viewModel.vehicleAdded(securityGuardId)
                dismiss()
            }

            is ParkingViewState.Error -> {
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

    private fun onResult(parkVehicleResponse: ParkVehicleResponse) {
        if (parkVehicleResponse.status==true) {
            Toast.makeText(
                requireContext(),
                "Successfully recorded parked vehicle",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                parkVehicleResponse.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val TAG = "AddParkingFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParkingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}