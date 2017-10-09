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
import android.support.annotation.BoolRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyCreateSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAgencyActivity extends BaseActivity {

    @BindView(R.id.estore)
    EditText eSotre;

    @BindView(R.id.edeputy)
    EditText eDeputy;

    @BindView(R.id.eidentitycard)
    EditText eIdentityCard;

    @BindView(R.id.ebusinesslicene)
    EditText eBusinessLicene;

    @BindView(R.id.ephone)
    EditText ePhone;

    @BindView(R.id.eaddress)
    EditText eAddress;

    @BindView(R.id.eprovince)
    EditText eProvince;

    @BindView(R.id.edistrict)
    EditText eDistrict;

    @BindView(R.id.imgAddress)
    ImageView imgAddress;

    @BindView(R.id.ecountry)
    EditText eCountry;

    @BindView(R.id.eward)
    EditText eWard;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.ec1)
    EditText eC1;

    @BindView(R.id.erank)
    Spinner eRank;

    @BindView(R.id.egroup)
    EditText eGroup;

    @BindView(R.id.etax)
    EditText eTax;
    @BindView(R.id.image)
    ImageView image;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    int C1_CODE = 2;

    double lat;
    double lng;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private MagicalPermissions magicalPermissions;
    MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;

    String path = "";

    boolean updateImageSuccess = false;

    String imageUpdatePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agency);
        createToolbar();
        ButterKnife.bind(this);
        createLocation();
        createCamera();
        lat = getCurrentLocation().getLatitude();
        lng = getCurrentLocation().getLongitude();
        new ReadDataTask().execute();

        imgAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(AddAgencyActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddAgencyActivity.this, FindAgencyC1Activity.class);
                startActivityForResult(i, C1_CODE);
            }
        });

        initERank();

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
    private void initERank() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.agency_rank, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eRank.setAdapter(adapter);
    }

    private void makeRequest(String imagePath) {

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");


        showpDialog();
        AgencyCreateSend info = new AgencyCreateSend();

        info.setUser(user);
        info.setToken(token);
        info.setLat(lat);
        info.setLng(lng);
        info.setC1Id(eC1.getText().toString());
        info.setDeputy(eDeputy.getText().toString());
        info.setName(eSotre.getText().toString());
        info.setBusinessLicense(eBusinessLicene.getText().toString());
        info.setIdentityCard(eIdentityCard.getText().toString());
        info.setAddress(eAddress.getText().toString());
        info.setCountry(eCountry.getText().toString());
        info.setProvince(eProvince.getText().toString());
        info.setDistrict(eDistrict.getText().toString());
        info.setWard(eWard.getText().toString());
        info.setGroup(Integer.parseInt(eGroup.getText().toString()));
        info.setRank(eRank.getSelectedItem().toString());
        info.setPhone(ePhone.getText().toString());
        info.setTaxCode(eTax.getText().toString());
        info.setImage(imagePath);

        Call<ResultInfo> call = apiInterface().createAgencyC2(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body().getId().equals("1")) {
                    commons.showAlertInfo(AddAgencyActivity.this, "Thông báo", "Đã gửi thông tin khách hàng, liên hệ quản trị để kích hoạt khách hàng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                } else {
                    commons.showAlertInfo(AddAgencyActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.makeToast(AddAgencyActivity.this, "Lỗi đường truyền");
            }
        });

    }

    private boolean checkRequire() {
        if (TextUtils.isEmpty(eDeputy.getText().toString())) {
            eDeputy.setError("Không để trống tên khách hàng");
            return false;
        }

        if (TextUtils.isEmpty(eSotre.getText().toString())) {
            eSotre.setError("Không để trống tên cửa hàng");
            return false;
        }

        if (TextUtils.isEmpty(eGroup.getText().toString())) {
            eGroup.setError("Không để trống cụm khách hàng");
            return false;
        }
        /*
        if (TextUtils.isEmpty(eC1.getText().toString())) {
            eC1.setError("Không để trống cấp 1");
            return false;
        }
        */
        if (TextUtils.isEmpty(ePhone.getText().toString())) {
            ePhone.setError( "Không để trống số điện thoại");
            return false;
        }
        if (TextUtils.isEmpty(eAddress.getText().toString())) {
            eAddress.setError("Không để trống địa chỉ");
            return false;
        }

        return true;
    }


    private class ReadDataTask extends AsyncTask<String, Integer, Address> {
        protected Address doInBackground(String... urls) {

            final Geocoder geocoder = new Geocoder(AddAgencyActivity.this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;

                new ReadDataTask().execute();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(AddAgencyActivity.class.getName(), status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == C1_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                eC1.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else {
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

                if (checkRequire())
                {
                    commons.showAlertCancel(AddAgencyActivity.this, "Cảnh báo", "Bạn muốn thêm khách hàng mới ?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (updateImageSuccess) {
                                makeRequest(imageUpdatePath);
                            } else {
                                if (!TextUtils.isEmpty(path)){
                                    uploadImage(path);
                                } else {
                                    commons.makeToast(AddAgencyActivity.this, "Chụp ảnh").show();
                                }
                            }
                        }
                    });
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Toast.makeText(AddAgencyActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });

    }

}
