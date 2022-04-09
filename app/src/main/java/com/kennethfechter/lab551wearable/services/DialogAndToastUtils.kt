package com.kennethfechter.lab551wearable.services

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings

class DialogAndToastUtils(context: Context) {
    var context: Context = context
    var dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

    fun showNFCDisabled() {
        dialogBuilder.setMessage("NFC is disabled, Do you want to enable it?")
            .setCancelable(false)

            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                context.startActivity(intent)
            }

            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
    }
}