package com.congtyhai.haidms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.congtyhai.haidms.login.LoginNameActivity;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {


    @BindView(R.id.image)
    ImageView imgLogo;
    boolean isSetting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        imgLogo.setAnimation(animHelper.animationFadeIn(getApplicationContext(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkAndRequestPermissions();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }));

    }

    private void openSettings() {
        isSetting = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void checkAndRequestPermissions() {


        Dexter.withActivity(SplashActivity.this).withPermissions(android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CALL_PHONE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    proceedAfterPermission();
                }

                // check for permanent denial of any permission
                if (report.isAnyPermissionPermanentlyDenied()) {
                    // show alert dialog navigating to Settings
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();


    }


    private void makeJsonRequest(final String user, final String token) {

        PackageInfo pinfo = null;

        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            Call<ResultInfo> call = apiInterface().checkSession(user, token, String.valueOf(versionNumber));

            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    if (response.body() != null) {
                        if ("1".equals(response.body().getId())) {
                            String type = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TYPE, "");
                            if ("STAFF".equals(type)) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(SplashActivity.this, MainAgencyActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        } else if ("2".equals(response.body().getId())) {
                            commons.showAlertCancelHandle(SplashActivity.this, "Thông báo", "Cập nhật phiên bản mới", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final String appPackageName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                        } else {
                            commons.showAlertCancelHandle(SplashActivity.this, "Thông báo", "Đăng nhập để truy cập ứng dụng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    commons.startActivity(SplashActivity.this, LoginNameActivity.class);
                                    finish();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    commons.showAlertCancelHandle(SplashActivity.this, "Thông báo", "Thử lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            makeJsonRequest(user, token);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                }
            });


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void proceedAfterPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, null);
                String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, null);
                if (TextUtils.isEmpty(token) || TextUtils.isEmpty(user)) {
                    makeJsonRequest("none", "none");
                } else {
                    makeJsonRequest(user, token);
                }
            }
        }, 100);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       if(isSetting) {
           checkAndRequestPermissions();
           isSetting = false;
       }
    }

}
