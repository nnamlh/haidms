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
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.congtyhai.haidms.login.LoginNameActivity;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {


    @BindView(R.id.image)
    ImageView imgLogo;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    String[] permissionsRequired = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CALL_PHONE

    };

    private boolean sentToSettings = false;

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
                checkPermission();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }));

    }

    private boolean checkSelf(String[] permiss) {

        for (int i = 0; i < permiss.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permiss[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }


    private boolean checkShowRequestPermisstionRationale(String[] permiss) {

        for (int i = 0; i < permiss.length; i++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permiss[i])) {
                return true;
            }
        }

        return false;
    }


    private void checkPermission() {

        if (!checkSelf(permissionsRequired)) {

            if (checkShowRequestPermisstionRationale(permissionsRequired)) {
                commons.showAlertCancelHandle(SplashActivity.this, "Cho phép truy cập", "Ứng dụng cần cho phép truy cập một số tác vụ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            } else {
                commons.showAlertCancelHandle(SplashActivity.this, "Cho phép truy cập", "Bạn cần đồng ý cho phép quyền truy cập để có thể sử dụng chương trình", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            }

        } else {
            proceedAfterPermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
                proceedAfterPermission();
            } else if (checkShowRequestPermisstionRationale(permissionsRequired)) {
                commons.showAlertCancelHandle(SplashActivity.this, "Cho phép truy cập", "Ứng dụng cần cho phép truy cập một số tác vụ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            } else {
                commons.showAlertCancelHandle(SplashActivity.this, "Cho phép truy cập", "Bạn cần đồng ý cho phép quyền truy cập để có thể sử dụng chương trình", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
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
                    commons.showAlertInfo(SplashActivity.this, "Thông báo", "Thử lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            makeJsonRequest(user, token);
                        }
                    });
                }
            });


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            checkPermission();
        }
    }

    private void proceedAfterPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, null);
                String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, null);
                if (TextUtils.isEmpty(token) || TextUtils.isEmpty(user)) {
                    //commons.startActivity(SplashActivity.this, LoginNameActivity.class);
                   // finish();
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
        if (sentToSettings) {
            checkPermission();
        }
    }

}
