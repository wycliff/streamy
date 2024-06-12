package com.movies.streamy.view.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.movies.streamy.databinding.FragmentHomeBinding
import com.movies.streamy.utils.Prefs
import com.movies.streamy.view.parking.ParkingActivity
import com.movies.streamy.view.tickets.TicketsActivity
import com.movies.streamy.view.visits.VisitsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    //view binding
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel


    private val binding get() = _binding
    private lateinit var prefs: Prefs

    private var securityGuardId: String? = null

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
        binding?.homeGrid?.let { setClickEvent(it) }
        initViews()
    }

    private fun initViews() {
        viewModel.currentUser.observe(requireActivity()) { currentUser ->
            securityGuardId = currentUser?.security_guard_id
            securityGuardId?.let {
                //get buildings and units
                viewModel.fetchBuildings(securityGuardId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setClickEvent(gridLayout: GridLayout) {
        for (i in 0 until gridLayout.childCount) {
            val cardView = gridLayout.getChildAt(i) as CardView
            cardView.setOnClickListener {

                when (i) {
                    0 -> {
                        val intent = Intent(requireContext(), VisitsActivity::class.java)
                        startActivity(intent)
                    }

                    1 -> {
                        val intent = Intent(requireContext(), TicketsActivity::class.java)
                        startActivity(intent)
                    }

                    2 -> {
                        val intent = Intent(requireContext(), ParkingActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}