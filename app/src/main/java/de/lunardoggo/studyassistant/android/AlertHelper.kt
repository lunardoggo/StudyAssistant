package de.lunardoggo.studyassistant.android

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class AlertHelper {
    companion object {
        public fun createYesNoAlert(context : Context, title : String, message : String, onClickListener : DialogInterface.OnClickListener) : AlertDialog {
            val builder = AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setTitle(title);

            builder.setPositiveButton("Yes", onClickListener);
            builder.setNegativeButton("No") { _interface, _int -> _interface.cancel(); };

            return builder.create();
        }
    }
}