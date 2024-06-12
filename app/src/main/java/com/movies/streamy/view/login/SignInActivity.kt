package com.movies.streamy.view.login


import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.movies.streamy.R
import com.movies.streamy.databinding.ActivitySignInBinding
import com.movies.streamy.utils.Constants.Companion.PHONE_METHOD
import com.movies.streamy.utils.getTrimmedText
import com.movies.streamy.utils.observe
import com.movies.streamy.utils.phoneIsEmpty
import com.movies.streamy.utils.pinIsEmpty
import com.movies.streamy.utils.snackbar
import com.movies.streamy.utils.validateLoginPhone
import com.movies.streamy.view.MainActivity
import com.movies.streamy.view.utils.BaseActivity
import com.movies.streamy.view.utils.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInActivity : BaseActivity() {
    //View binding
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var signInViewModel: SignInViewModel

    private var phoneNo = ""
    private var code = ""
    private var loginMethod = PHONE_METHOD
    private val progressDialog = ProgressDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        initViews()
    }


    private fun initViews() {
        binding.ccp.registerCarrierNumberEditText(binding.editTextMobileNo)
        observe(signInViewModel.viewState, ::onViewStateChanged)
        binding.btnSignIn.setOnClickListener {
            validateFields()
        }

        binding.etPin.transformationMethod = PasswordTransformationMethod()
    }

    private fun validateFields() {
        binding.editTextMobileNo.error = null
        if (binding.editTextMobileNo.phoneIsEmpty(loginMethod)) {
            return
        }

        if (binding.editTextMobileNo.let {
                binding.ccp.validateLoginPhone(
                    it,
                    loginMethod
                )
            }) {
            return
        }

        binding.etPin.error = null
        if (binding.etPin.pinIsEmpty()) {
            return
        }

        phoneNo = binding.ccp.fullNumberWithPlus.toString()
        code = binding.tlPin.getTrimmedText()

        signInViewModel.login(phoneNo, code)
    }

    private fun onViewStateChanged(state: SignInViewState) {
        when (state) {
            is SignInViewState.Loading -> {
                binding.btnText.visibility = View.GONE
                binding.loading.visibility = View.VISIBLE
            }

            is SignInViewState.Success -> {
                binding.loading.visibility = View.GONE
                binding.btnText.visibility = View.VISIBLE

                //Navigate to main
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            is SignInViewState.LoginError -> {
                val message = state.errorMessage ?: state.stringResourceId?.let { getString(it) }
                binding.loading.visibility = View.GONE
                binding.btnText.visibility = View.VISIBLE
                showSnackBar(message, false)
            }

            else -> {}
        }
    }

    private fun showSnackBar(message: String?, isSuccess: Boolean = true) {
        binding.root.let {
            this.snackbar(
                it,
                message,
                if (isSuccess) R.color.md_success_color else R.color.md_error_color
            )
        }
    }
}