package com.movies.streamy.view.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.movies.streamy.R
import com.movies.streamy.utils.Prefs
import com.movies.streamy.view.MainActivity
import com.movies.streamy.view.login.SignInActivity
import com.movies.streamy.view.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var prefs: Prefs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        prefs = Prefs(this)
        routeApp()
    }

    private fun routeApp() {
        lifecycleScope.launch {
            splashViewModel.currentUser?.observe(this@SplashActivity) { currentUser ->
                if (currentUser == null) {
                    startSignInActivity()
                } else {
                    startMainActivity(true)
                }
            }
        }
    }

    private fun startMainActivity(isAdmin: Boolean?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val animation = ActivityOptions.makeCustomAnimation(
            applicationContext,
            R.anim.cb_fade_in,
            R.anim.cb_face_out
        ).toBundle()
        startActivity(intent, animation)
        finish()
    }

    private fun startSignInActivity() {
        val x = Intent(applicationContext, SignInActivity::class.java)
        val animation = ActivityOptions.makeCustomAnimation(
            applicationContext,
            R.anim.cb_fade_in,
            R.anim.cb_face_out
        ).toBundle()
        startActivity(x, animation)
        finish()
    }
}



