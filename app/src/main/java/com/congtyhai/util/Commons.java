package com.congtyhai.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

    public void showToastDisconnect(final Context context) {
        Toast.makeText(context, "Mất kết nối internet", Toast.LENGTH_LONG).show();
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

    public void showAlertCancelHandle(Activity activity, String tile, String content, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancel) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setIcon(R.mipmap.ic_logo);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener).setNegativeButton("Thôi", cancel);

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

    public void writeFile(String json, String path, Activity activity) {
        try {

            if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                File file = new File(activity.getExternalFilesDir("NONGDUOCHAI"), path);

                if (!file.exists()) {
                    file.mkdirs();
                }

                FileWriter writer = new FileWriter(file.getAbsoluteFile() + path);
                writer.write(json);
                writer.close();
            }

            /*
            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "NONGDUOCHAI");
*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void deleteFile(String path, Activity activity) {
        try {

            if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                File file = new File(activity.getExternalFilesDir("NONGDUOCHAI"), path);

                if (file != null){
                    file.delete();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public BufferedReader readBufferedReader(String path, Activity activity) {
        try {
            if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {

                File file = new File(activity.getExternalFilesDir("NONGDUOCHAI"), path);

                BufferedReader br = new BufferedReader(
                        new FileReader(file.getAbsoluteFile() +  path));

                return  br;
            }

            /*
            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "NONGDUOCHAI");*/

        } catch (IOException e) {
            return null;
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


    public String getOrderDetailText(int box, int quantity, String unit) {
        int countCan = quantity / box;
        int countBox = quantity - countCan*box;

        if (countCan == 0) {
            return countBox + " " + unit;
        }

        if (countBox == 0) {
            return countCan + " thùng";
        }

        return countCan + " thùng " + countBox + " " + unit;

    }



    public double distance(double lat1, double lon1, double lat2, double lon2) {

    /*
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return (Math.round(dist));


        float pk = (float) (180.f/Math.PI);

        double a1 = lat1 / pk;
        double a2 = lon1 / pk;
        double b1 = lat2 / pk;
        double b2 = lon2 / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
        */


        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);

        double distance=startPoint.distanceTo(endPoint);

        return (Math.round(distance));
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
