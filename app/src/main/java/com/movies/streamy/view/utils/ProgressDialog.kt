package com.movies.streamy.view.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.movies.streamy.R

class ProgressDialog {
    private lateinit var dialog: Dialog
    private val nullParent: ViewGroup? = null
    fun show(context: Context): Dialog {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.progress_dialog, nullParent)
        dialog = Dialog(context, R.style.LoadingDialog)
        dialog.setContentView(view)
        dialog.window?.setGravity(Gravity.CENTER_HORIZONTAL)
        dialog.setCancelable(false)
        dialog.show()

        return dialog
    }

    fun cancel() {
        try {
            dialog.dismiss()
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }

    }

}