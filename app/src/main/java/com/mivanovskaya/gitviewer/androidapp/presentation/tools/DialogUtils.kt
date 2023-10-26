package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R

fun showAlertDialog(message: StringValue, context: Context) {
    val dialog =
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_Material_MaterialAlertDialog)
            .setCancelable(true).setTitle(context.getString(R.string.error))
            .setMessage(message.asString(context))
            .setPositiveButton(context.getString(R.string.ok)) { _, _ -> }.create()
    dialog.show()
}
