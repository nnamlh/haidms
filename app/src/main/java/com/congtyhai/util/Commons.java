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

import com.congtyhai.haidms.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by HAI on 8/10/2017.
 */

public final class Commons {

    public Snackbar makeSnackbar(final View view, final String message) {
        return Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    }

    public Toast makeToast(final Context context, final String message) {
        return Toast.makeText(context, message, Toast.LENGTH_SHORT);

    }

    public void showAlertCancel(Activity activity, String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setIcon(R.mipmap.ic_logo);
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
        dialog.setIcon(R.mipmap.ic_logo);
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
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "NONGDUOCHAI");

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

    public BufferedReader readBufferedReader(String path) {
        try {

            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "NONGDUOCHAI");
            BufferedReader br = new BufferedReader(
                    new FileReader(file.getAbsoluteFile() +  path));

            return  br;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

    public long distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (Math.round(dist));
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public  double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
