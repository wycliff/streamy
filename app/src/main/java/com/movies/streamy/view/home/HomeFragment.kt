package com.movies.streamy.view.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.streamy.databinding.FragmentHomeBinding
import com.movies.streamy.model.dataSource.network.data.response.MovieId
import com.movies.streamy.utils.Prefs
import com.movies.streamy.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    //view binding
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    private val binding get() = _binding!!
    private lateinit var prefs: Prefs

    private var securityGuardId: String? = null

    private val movieAdapter =
        MovieIdAdapter { data: MovieId -> itemClicked(data) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        prefs = Prefs(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        showShimmerEffect()
        viewModel.getMovieIds()
        setUpObservers()
        setUpAdapter()
    }

    private fun setUpObservers() {
        observe(viewModel.movieIds, ::setUpRecyclerView)
        observe(viewModel.viewState, ::onViewStateChanged)
    }

    private fun setUpAdapter() {
        binding.rvMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter?.setHasStableIds(true)
            adapter = movieAdapter
        }
    }

    private fun setUpRecyclerView(movieIdList: List<MovieId?>?) {
        if (movieIdList?.isEmpty() == true) {
            binding.noDataGroup.visibility = View.VISIBLE
        } else {
            hideShimmerEffect()
            movieAdapter.submitList(movieIdList)
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
//                showSnackBar(state.errorMessage, false)
                binding.noTextOrder.visibility = View.VISIBLE
                binding.noImageOrder.visibility = View.VISIBLE
            }

            else -> {}
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvMovies.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvMovies.visibility = View.VISIBLE
    }

    private fun itemClicked(data: MovieId) {
        //todo
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun showSnackBar(message: String?, isSuccess: Boolean = true) {
//        binding.root.let {
//            this.snackbar(
//                it,
//                message,
//                if (isSuccess) R.color.md_info_color else R.color.md_error_color
//            )
//        }
//    }
}