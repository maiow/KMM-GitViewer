package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R

fun showAlertDialog(message: String, context: Context) {
    val dialog =
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_Material_MaterialAlertDialog)
            .setCancelable(true).setTitle(context.getString(R.string.error))
            .setMessage(context.getString(R.string.error_description, message))
            .setPositiveButton(context.getString(R.string.ok)) { _, _ -> }.create()
    dialog.show()
}
