package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R

fun Context.hideKeyboard(view: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun setAppColor(context: Context, color: Int) = ContextCompat.getColor(context, color)

fun setBackgroundAppColor(context: Context, color: Int) =
    ColorStateList.valueOf(ContextCompat.getColor(context, color))

fun showAlertDialog(message: String, context: Context) {
    val dialog =
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_Material_MaterialAlertDialog)
            .setCancelable(true).setTitle(context.getString(R.string.error))
            .setMessage(context.getString(R.string.error_description, message))
            .setPositiveButton(context.getString(R.string.ok)) { _, _ -> }.create()
    dialog.show()
}
