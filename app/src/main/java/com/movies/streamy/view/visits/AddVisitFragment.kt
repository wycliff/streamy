package com.movies.streamy.view.visits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentAddVisitBinding
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.dataSource.network.data.request.SecurityGuard
import com.movies.streamy.model.dataSource.network.data.request.SignInRequest
import com.movies.streamy.model.dataSource.network.data.request.Tenant
import com.movies.streamy.model.dataSource.network.data.request.Vehicle
import com.movies.streamy.model.dataSource.network.data.request.Visitor
import com.movies.streamy.utils.Constants.Companion.KEY_IS_UNIT_REQUEST
import com.movies.streamy.utils.Constants.Companion.KEY_TENANT_ID
import com.movies.streamy.utils.getTrimmedText
import com.movies.streamy.utils.observe
import com.movies.streamy.view.home.HomeViewModel
import com.movies.streamy.view.home.HomeViewState
import com.movies.streamy.view.utils.ViewUtils.Companion.showSnackBar
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddVisitFragment : BottomSheetDialogFragment() {


    //View binding
    private var _binding: FragmentAddVisitBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var securityGuardId: String? = null

    private var currentUser: UserEntity? = null
    private var tenantId: String? = null
    private var buildingId: String? = null

    private var isOriginUnit: Boolean = false

    private var visitorName = ""
    private var visitorNumber = ""
    private var visitorEmail = ""
    private var visitorId = ""

    private var vehicleRegistration = ""
    private var vehicleColor = ""
    private var vehicleModel = ""
    private var vehicleOccupants = 0


    //Buildings spinner
    private val buildingsDisplayNames = mutableListOf<IconSpinnerItem>()
    private var selectedBuildingName: String? = null

    //Units spinner
    private val houseDisplayNames = mutableListOf<IconSpinnerItem>()
    private var selectedHouseName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        arguments?.let {
            isOriginUnit = it.getBoolean(KEY_IS_UNIT_REQUEST)
            tenantId = it.getString(KEY_TENANT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddVisitBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        viewModel.getAllBuildings()

        if (isOriginUnit) {
            binding.tvBuilding.visibility = View.GONE
            binding.spBuildings.visibility = View.GONE
        }

        viewModel.currentUser.observe(requireActivity()) { currentUserItem ->
            currentUser = currentUserItem
        }

        setUpObservers()
        binding.btAddVisitor.setOnClickListener {
            if (validateForm()) {
                visitorName = binding.tlVisitorName.getTrimmedText()
                visitorNumber = binding.tlVisitorPhone.getTrimmedText()
                visitorEmail = binding.tlVisitorEmail.getTrimmedText()
                visitorId = binding.tlVisitorId.getTrimmedText()

                vehicleRegistration = binding.tlVehicleReg.getTrimmedText()
                vehicleModel = binding.tlVehicleModel.getTrimmedText()
                vehicleColor = binding.tlVehicleColour.getTrimmedText()

                if (!binding.etVehicleOuccupants.text.isNullOrEmpty())
                    vehicleOccupants = binding.tlVehicleOccupants.getTrimmedText().toInt()

                val securityGuard = SecurityGuard("", currentUser?.security_guard_id)
                val visitor = Visitor(
                    visitorEmail,
                    visitorId,
                    visitorName,
                    visitorNumber
                )
                val vehicle =
                    Vehicle(vehicleColor, vehicleModel, vehicleOccupants, vehicleRegistration)
                val tenant = Tenant(tenantId)
                val signInRequest = SignInRequest(securityGuard, tenant, vehicle, visitor)
                viewModel.signInVisitor(signInRequest)

            }
        }

        binding.llAddVehicle.setOnClickListener {
            binding.tvVehicleReg.visibility = View.VISIBLE
            binding.tlVehicleReg.visibility = View.VISIBLE

            binding.tvVehicleModel.visibility = View.VISIBLE
            binding.tlVehicleModel.visibility = View.VISIBLE

            binding.tvVehicleOccupants.visibility = View.VISIBLE
            binding.tlVehicleOccupants.visibility = View.VISIBLE

            binding.tvVehicleColour.visibility = View.VISIBLE
            binding.tlVehicleColour.visibility = View.VISIBLE
        }
    }

    private fun validateForm(): Boolean {
        if (binding.etVisitorName.text.isNullOrEmpty()) {
            binding.etVisitorName.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        if (binding.etVisitorPhone.text.isNullOrEmpty()) {
            binding.etVisitorPhone.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        if (binding.etVisitorId.text.isNullOrEmpty()) {
            binding.etVisitorId.error = requireContext().getString(R.string.text_required_field)
            return false
        }
        if (buildingId.isNullOrEmpty() && !isOriginUnit) {
            Toast.makeText(
                requireContext(),
                getString(R.string.text_error_building),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (tenantId.isNullOrEmpty() && !isOriginUnit) {
            Toast.makeText(requireContext(), getString(R.string.text_error_host), Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    private fun setUpObservers() {
        observe(viewModel.buildings, ::onBuildings)
        observe(viewModel.units, ::onUnits)
        observe(viewModel.viewState, ::onViewState)
    }

    private fun onViewState(state: HomeViewState) {
        when (state) {
            is HomeViewState.Loading -> {
                binding.loading.visibility = View.VISIBLE
                binding.btnText.visibility = View.GONE
            }

            is HomeViewState.Success -> {
                binding.loading.visibility = View.VISIBLE
                binding.btnText.visibility = View.GONE
                Toast.makeText(requireContext(), "Successfully added visitor", Toast.LENGTH_LONG).show()
                dismiss()
            }

            is HomeViewState.Error -> {
                binding.loading.visibility = View.GONE
                binding.btnText.visibility = View.VISIBLE
                showSnackBar(state.errorMessage, false, requireContext(), binding.root)
            }

            else -> {}
        }
    }

    private fun onBuildings(buildings: List<BuildingEntity>) {
        buildingsDisplayNames.clear()

        for (building in buildings) {
            building.buildingName?.let { IconSpinnerItem(it) }?.let { houseDisplayNames.add(it) }
        }

        binding.spBuildings.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(houseDisplayNames)
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)
        }

        binding.spBuildings.setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->
            newItem.text.toString()
            binding.tvHouseNumber.visibility = View.VISIBLE
            binding.spUnit.visibility = View.VISIBLE

            selectedHouseName = newItem.text.toString()
            buildings[newIndex].buildingId.let {
                buildingId = it
                viewModel.getUnits(it)
            }
        }
    }

    private fun onUnits(units: List<UnitEntity>) {
        houseDisplayNames.clear()

        val trimmedUnits = mutableListOf<UnitEntity>()

        for (unit in units) {
            if (unit.ocupancyStatus.equals("Occupied")) {
                trimmedUnits.add(unit)
                houseDisplayNames.add(IconSpinnerItem(unit.tenantName + " " + unit.unitName))
            }
        }

        binding.spUnit.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(houseDisplayNames)
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)
        }

        binding.spUnit?.setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->
            newItem.text.toString()
            selectedHouseName = newItem.text.toString()
            trimmedUnits[newIndex].tenantId.let {
                tenantId = it
            }
        }
    }

    companion object {
        const val TAG = "AddVisitFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddVisitFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}