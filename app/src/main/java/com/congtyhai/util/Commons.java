package com.congtyhai.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by HAI on 8/10/2017.
 */

public final class Commons {

    public Snackbar makeSnackbar(final View view, final String message) {
        return Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    }

    public Toast makeToast(final Context context, final String message, int type) {
     //   return Toast.makeText(context, message, Toast.LENGTH_SHORT);
        return TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, type);
    }

    public void showAlertCancel(Activity activity, String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showAlertInfo(Activity activity,String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void writeFile(String json, String path) {
        try {
            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");

            if (!file.exists()) {
                file.mkdirs();
            }

            FileWriter writer = new FileWriter(file.getAbsoluteFile() + path);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);


        activity.startActivity(intent);
    }

    public Intent createIntent(Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        return intent;
    }

    public String getPhone(Context context) {
        TelephonyManager phoneManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String phoneNumber = phoneManager.getLine1Number();
        return phoneNumber;
    }
}
