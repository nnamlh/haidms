package com.congtyhai.haidms.Agency;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.checkin.DecorImageActivity;
import com.congtyhai.model.api.AgencyUpdateLocationSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.app.HaiLocation;
import com.congtyhai.util.HAIRes;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgencyUpdateLocationActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.eaddress)
    EditText eAddress;

    @BindView(R.id.eprovince)
    EditText eProvince;

    @BindView(R.id.edistrict)
    EditText eDistrict;

    @BindView(R.id.eward)
    EditText eWard;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.ecountry)
    EditText eCountry;
    double lat;
    double lng;

    String agencyId;

    private GoogleMap mMap;

    boolean updateImageSuccess = false;

    String imageUpdatePath = "";

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private MagicalPermissions magicalPermissions;
    MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;

    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_update_location);
        createToolbar();
        ButterKnife.bind(this);
        createLocation();

        Intent intent = getIntent();
        agencyId = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE);

        lat = getCurrentLocation().getLatitude();
        lng = getCurrentLocation().getLongitude();
        new ReadDataTask().execute();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createCamera();
    }

    public void takeImage(View view) {
        magicalCamera.takePhoto();
    }

    private void createCamera() {
        magicalPermissions = new MagicalPermissions(this, permissions);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //TODO location permissions are granted code here your feature
                Toast.makeText(getApplicationContext(), "Thanks for granting location permissions", Toast.LENGTH_LONG).show();
            }
        };
        magicalPermissions.askPermissions(runnable);


        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "NDH");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(), timeStamp, "NONGDUOCHAI", MagicalCamera.JPEG, true);


        previewMedia(path);


    }

    private void previewMedia(String filePath) {
        Uri imageUri = Uri.parse(filePath);
        File file = new File(imageUri.getPath());

        try {
            InputStream ims = new FileInputStream(file);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            final Bitmap bitmap = BitmapFactory.decodeStream(ims, null, options);

            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
        //Following the example you could also
        //locationPermissions(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(7.0f);
        mMap.setMaxZoomPreference(16.0f);
        HaiLocation location = getCurrentLocation();
        LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(me).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
    }

    private class ReadDataTask extends AsyncTask<String, Integer, Address> {
        protected Address doInBackground(String... urls) {

            final Geocoder geocoder = new Geocoder(AgencyUpdateLocationActivity.this);

            final List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(lat,
                        lng, 1);

            } catch (IOException e) {
                return null;
            }
            if (addresses != null && !addresses.isEmpty())
                return addresses.get(0);
            else
                return null;

        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(Address result) {

            if (result != null) {
                String strAddress = result.getSubThoroughfare() + " " + result.getThoroughfare() + " , " + result.getSubLocality();
                eAddress.setText(strAddress);
                eProvince.setText(result.getAdminArea());
                eDistrict.setText(result.getSubAdminArea());
                eWard.setText(result.getSubLocality());
                eCountry.setText(result.getCountryCode());
            }

            hidepDialog();
        }

    }


    private void makeRequest(String imagePath) {
        showpDialog();
        String muser = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String mtoken = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        AgencyUpdateLocationSend info = new AgencyUpdateLocationSend();
        info.setId(agencyId);
        info.setLat(lat);
        info.setLng(lng);
        info.setAddress(eAddress.getText().toString());
        info.setProvince(eProvince.getText().toString());
        info.setDistrict(eDistrict.getText().toString());
        info.setWard(eWard.getText().toString());
        info.setCountry(eCountry.getText().toString());
        info.setUser(muser);
        info.setToken(mtoken);

        info.setImage(imagePath);

        Call<ResultInfo> call = apiInterface().updateAgencyLocation(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        commons.showAlertInfo(AgencyUpdateLocationActivity.this, "Thông báo", "Đã cập nhật", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent returnIntent = getIntent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        });
                    } else {
                        commons.showAlertInfo(AgencyUpdateLocationActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

            }
        });


    }


    private void uploadImage(String filePath) {

        showpDialog();
        String muser = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String mtoken = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        File sourceFile = null;
        RequestBody requestFile = null;
        MultipartBody.Part body = null;
        if (filePath != null) {
            Uri imageUri = Uri.parse(filePath);
            sourceFile = new File(imageUri.getPath());

            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
            body =
                    MultipartBody.Part.createFormData("image", sourceFile.getName(), requestFile);

        }

        RequestBody user =
                RequestBody.create(
                        MediaType.parse("text/plain"), muser);

        RequestBody token =
                RequestBody.create(
                        MediaType.parse("text/plain"), mtoken);

        RequestBody extension =
                RequestBody.create(
                        MediaType.parse("text/plain"), ".jpg");

        RequestBody folder =
                RequestBody.create(
                        MediaType.parse("text/plain"), "1");


        Call<ResultInfo> call = apiInterfaceUpload().uploadImage2(body, user, token, extension, folder);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        imageUpdatePath = response.body().getMsg();
                        updateImageSuccess = true;
                        makeRequest(response.body().getMsg());
                    } else {
                        Toast.makeText(AgencyUpdateLocationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.next_action:
                if (updateImageSuccess) {
                   makeRequest(imageUpdatePath);
                } else {
                   if (!TextUtils.isEmpty(path)){
                       uploadImage(path);
                   } else {
                       commons.makeToast(AgencyUpdateLocationActivity.this, "Chụp ảnh").show();
                   }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


}
