package com.movies.streamy.view.buildings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentOrdersBinding
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.utils.Constants.Companion.KEY_BUILDING_ID
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class BuildingsFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ActionsViewModel
    private var securityGuardId: String? = null

    private val buildingsAdapter =
        BuildingsAdapter { data: BuildingEntity -> itemClicked(data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ActionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        showShimmerEffect()
        getCurrentUser()
        viewModel.getBuildings()

        addObservers()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchBuildingsFromApi(securityGuardId)
        }

        setUpAdapter()
    }


    private fun getCurrentUser() {
        lifecycleScope.launch {
            viewModel.currentUser.observe(requireActivity()) { currentUser ->
                securityGuardId = currentUser?.security_guard_id
            }
        }
    }

    private fun setUpAdapter() {
        binding.rvBuildings.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = buildingsAdapter
        }
    }


    private fun addObservers() {
        observe(viewModel.buildings, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewState)
    }

    private fun setUpRecyclerView(buildingList: List<BuildingEntity>) {
        if (buildingList.isEmpty()) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            buildingsAdapter.submitList(buildingList)
        }
    }

    private fun onViewState(viewState: BuildingsViewState) {
        binding.swipeRefresh.isRefreshing = false
        when (viewState) {
            is BuildingsViewState.Loading -> {}
            is BuildingsViewState.Success -> {
                binding.swipeRefresh.isRefreshing = false
                viewModel.getBuildings()
            }

            is BuildingsViewState.Error -> {
                binding.swipeRefresh.isRefreshing = false }
            }
        }

        private fun itemClicked(data: BuildingEntity) {
            val intent = Intent(requireContext(), UnitsActivity::class.java)
            intent.putExtra(KEY_BUILDING_ID, data.buildingId)
            startActivity(intent)
        }

        private fun showShimmerEffect() {
            binding.shimmerFrameLayout.startShimmer()
            binding.shimmerFrameLayout.visibility = View.VISIBLE
        }

        private fun hideShimmerEffect() {
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.GONE
        }

        private fun showSnackBar(message: String?, warning: Boolean = true) {
            binding.root.let {
                requireContext().snackbar(
                    it,
                    message,
                    if (warning) R.color.md_info_color else R.color.md_error_color
                )
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }