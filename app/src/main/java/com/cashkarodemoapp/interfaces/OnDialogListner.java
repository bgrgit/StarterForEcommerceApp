package com.cashkarodemoapp.interfaces;

import android.content.DialogInterface;

/**
 * Created by NSM Services on 8/18/16.
 */
public interface OnDialogListner {

    public void onPositiveButtonClick(DialogInterface dialog,String whichDialog);
    public void onNegativeButtonClick(DialogInterface dialog,String whichDialog);

}
