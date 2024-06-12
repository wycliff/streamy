@file:Suppress("KDocUnresolvedReference", "Annotator")

package com.movies.streamy.utils

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.movies.streamy.R
import com.movies.streamy.utils.Constants.Companion.PHONE_METHOD


fun CountryCodePicker.validateLoginPhone(editText: EditText, loginMethod: String): Boolean {
    if (!this.isValidFullNumber && loginMethod == PHONE_METHOD) {
        editText.error = this.context.getString(R.string.text_phone)
        this.requestFocus()
        return true
    }
    return false
}

fun EditText.phoneIsEmpty(loginMethod: String): Boolean {
    if (TextUtils.isEmpty(this.text) && loginMethod == PHONE_METHOD) {
        this.error = this.context.getString(R.string.text_required_field)
        this.requestFocus()
        return true
    }
    return false
}

fun EditText.pinIsEmpty(): Boolean {
    if (TextUtils.isEmpty(this.text)) {
        this.error = this.context.getString(R.string.text_required_field)
        this.requestFocus()
        return true
    }
    return false
}

fun TextInputLayout.getTrimmedText(): String {
    return this.editText?.text.toString().trim().replace("\\s".toRegex(), "").replace(" ", "")
}

fun TextInputLayout.getMyText(): String {
    return this.editText?.text.toString().trim()
}

/**
 * Shows [SnackBar] with provided message
 * @param view - The parent view to attach the SnackBar to
 * @param message - Message to display
 * @param backgroundTint - Tint to apply to SnackBar, default is green
 */
fun Context.snackbar(view: View, message: String?, backgroundTint: Int = R.color.md_success_color) {
    if (message.isNullOrEmpty()) return

    val snackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
        .apply {
            this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                ?.apply {
                    maxLines = 5
                    textSize = 15F
                    isSingleLine = false
                }
        }
    snackbar.setBackgroundTint(ContextCompat.getColor(this, backgroundTint))
    snackbar.show()
}

/**
 * Shows [SnackBar] with provided message
 * @param view - The parent view to attach the SnackBar to
 * @param message - Message to display
 * @param backgroundTint - Tint to apply to SnackBar, default is green
 */
fun Context.snackBarWithDuration(
    view: View,
    message: String?,
    backgroundTint: Int = R.color.md_success_color,
    duration: Int,
) {
    if (message.isNullOrEmpty()) return

    val snackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
        .apply {
            this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                ?.apply {
                    maxLines = 5
                    textSize = 15F
                    isSingleLine = false
                }
        }
        .setDuration(duration)
    snackbar.setBackgroundTint(ContextCompat.getColor(this, backgroundTint))
    snackbar.show()
}


fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}
