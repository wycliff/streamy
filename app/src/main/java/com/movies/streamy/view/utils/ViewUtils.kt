package com.movies.streamy.view.utils

import android.content.Context
import android.view.View
import com.movies.streamy.R
import com.movies.streamy.utils.snackbar

class ViewUtils {
    companion object {
        fun showSnackBar(
            message: String?,
            isSuccess: Boolean = true,
            context: Context,
            view: View
        ) {
            view.let {
                context.snackbar(
                    it,
                    message,
                    if (isSuccess) R.color.md_info_color else R.color.md_error_color
                )
            }
        }
    }
}