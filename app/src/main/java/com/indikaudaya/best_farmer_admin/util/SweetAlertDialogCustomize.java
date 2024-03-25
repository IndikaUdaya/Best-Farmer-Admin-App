package com.indikaudaya.best_farmer_admin.util;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetAlertDialogCustomize {

    public void successAlert(Context context, String successMsg) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(successMsg)
                .show();
    }

    public SweetAlertDialog confirmationAlert(Context context, String title, String buttonName) {
        return new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText(title)
                .setConfirmText(buttonName)
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation);
    }

    public void errorAlert(Context context, String errorMsg) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(errorMsg)
                .show();
    }

    public SweetAlertDialog loadingAlert(Context context, boolean cancelable) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please wait...");
        pDialog.setCancelable(cancelable);
        return pDialog;
    }

    public SweetAlertDialog confirmSuccessAlert(Context context, String subTitle, String confirmTextButton) {
        return new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText(subTitle)
                .setConfirmText(confirmTextButton);
    }
}





