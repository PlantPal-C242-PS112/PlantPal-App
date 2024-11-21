package com.android.plantpal.ui.utils.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.android.plantpal.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FailedDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun startFailedDialog(message: String, autoDismiss: Boolean = true, delayMillis: Long = 2000) {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.failed_dialog, null)

        val messageTextView: TextView = view.findViewById(R.id.message)
        messageTextView.text = message

        builder.setView(view)
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()

        if (autoDismiss) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(delayMillis)
                dismissDialog()
            }
        }
    }

    private fun dismissDialog() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}
