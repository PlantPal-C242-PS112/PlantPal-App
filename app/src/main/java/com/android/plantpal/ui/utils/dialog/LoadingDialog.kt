package com.android.plantpal.ui.utils.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.android.plantpal.R

class LoadingDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading, null))
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}
