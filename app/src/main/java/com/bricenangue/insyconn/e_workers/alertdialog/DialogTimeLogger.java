package com.bricenangue.insyconn.e_workers.alertdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.bricenangue.insyconn.e_workers.interfaces.OnDialogSelectorListener;

/**
 * Created by bricenangue on 26.08.17.
 */

public class DialogTimeLogger {
    private Context context;
    private int selection;
    private OnDialogSelectorListener onDialogSelectorListener;

    public DialogTimeLogger(Context context, OnDialogSelectorListener onDialogSelectorListener) {
        this.context=context;
        this.onDialogSelectorListener=onDialogSelectorListener;

    }

    public Dialog onCreateDialogTimeLogger(final boolean isarrived){
        //Initialize the Alert Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //Source of the data in the DIalog
        CharSequence[] array ;
        if (isarrived){
            array= new CharSequence[]{"On Break", "Leaving"};
        }else {
            array = new CharSequence[]{"Arriving", "On Break", "Leaving"};
        }

        // Set the dialog title
        builder.setTitle("Select an action")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection= which;
                    }
                })

                // Set the action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the result somewhere
                        // or return them to the component that opened the dialog
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if (isarrived){
                            onDialogSelectorListener.onSelectedOption(selectedPosition + 1);

                        }else {
                            onDialogSelectorListener.onSelectedOption(selectedPosition);

                        }
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

}
