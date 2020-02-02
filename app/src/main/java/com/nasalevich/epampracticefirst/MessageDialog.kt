package com.nasalevich.epampracticefirst

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MessageDialog : DialogFragment() {

    private var callback: MessageDialogCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is MessageDialogCallback) {
            callback = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(R.string.clear_dialog_text)
            .setPositiveButton(
                R.string.clear_dialog_confirm
            ) { _, _ ->
                callback?.onPositiveClick()
            }
            .setNegativeButton(
                R.string.clear_dialog_cancel
            ) { _, _ ->
            }
            .create()
    }

    interface MessageDialogCallback {
        fun onPositiveClick()
    }
}