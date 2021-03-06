package com.example.jobquestV1;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class VerificationDialog {

    private Activity activity;
    private AlertDialog dialog;

    VerificationDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.verificationdialog, null));
        builder.setCancelable(true); // user wont be able to tap out of the loading dialog

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
