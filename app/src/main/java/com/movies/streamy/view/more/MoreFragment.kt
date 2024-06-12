package com.movies.streamy.view.more

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movies.streamy.R
import com.movies.streamy.databinding.FragmentMoreBinding
import com.movies.streamy.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding

    private lateinit var moreViewModel: MoreViewModel
    private var securityGuardId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moreViewModel = ViewModelProvider(this)[MoreViewModel::class.java]
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        moreViewModel.currentUser.observe(requireActivity()) { currentUser ->
            currentUser?.let {
                binding?.tvUserName?.text = it.firstName
                binding?.tvCompanyName?.text = it.securityCompanyName
                binding?.profileImage?.text = it.firstName?.get(0).toString()
            }
        }

        binding?.clLogout?.setOnClickListener {
            logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        val builder = MaterialAlertDialogBuilder(
            requireActivity(),
            R.style.ThemeOverlay_App_MaterialAlertDialog_Update_App
        )
        builder.setCancelable(false)
        builder.setMessage(resources.getString(R.string.logout_message))
        builder.setPositiveButton(
            resources.getString(R.string.logout)
        ) { _: DialogInterface?, _: Int ->

            //todo: update online status (switch off)
            moreViewModel.logout()

            val x = Intent(requireContext(), SplashActivity::class.java)
            val animation = ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.cb_fade_in,
                R.anim.cb_face_out
            ).toBundle()

            startActivity(x, animation)
            activity?.finish()
        }
        builder.setNegativeButton(
            resources.getString(R.string.cancel)
        ) { _: DialogInterface?, _: Int ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}