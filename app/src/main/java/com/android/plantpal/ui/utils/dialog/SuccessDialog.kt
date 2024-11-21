package com.android.plantpal.ui.utils.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.android.plantpal.R

class SuccessDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun startSuccessDialog(message: String) {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.success_dialog, null)

        val messageTextView: TextView = view.findViewById(R.id.message)
        messageTextView.text = message

        builder.setView(view)
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}