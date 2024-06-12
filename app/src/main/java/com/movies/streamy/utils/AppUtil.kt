package com.movies.streamy.utils

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.movies.streamy.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import timber.log.Timber
import kotlin.math.roundToInt

object AppUtil {

    fun hideKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun hasGps(activity: Activity): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Timber.w(e)
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            Timber.w(e)
        }

        return gpsEnabled && networkEnabled
    }

    fun roundDown(double: Double): Double {
        return (double * 1000).roundToInt().toDouble() / 1000
    }

    /**
     * Shows [SnackBar] with provided message
     * @param view - The parent view to attach the SnackBar to
     * @param message - Message to display
     * @param backgroundTint - Tint to apply to SnackBar, default is red
     */
    fun Context.snackBar(view: View, message: String?, backgroundTint: Int = R.color.error_color) {
        if (message.isNullOrEmpty()) return

        val snackBar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
            .apply {
                this.view.findViewById<TextView>(R.id.snackbar_text)
                    ?.apply {
                        maxLines = 5
                        textSize = 15F
                        isSingleLine = false
                    }
            }
        snackBar.setBackgroundTint(ContextCompat.getColor(this, backgroundTint))
        snackBar.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }

    fun getErrorResponse(errorResponse: ErrorResponse?): String {
        return errorResponse?.message.toString()
    }
}