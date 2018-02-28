package com.congtyhai.haidms.checkin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import com.congtyhai.adapter.DecorImageAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.DecorImage;
import com.congtyhai.model.api.DecorImageSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;
import com.congtyhai.view.SlideshowDialogFragment;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class DecorImageActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    DecorImageAdapter adapter;
    List<DecorImage> decorImages;
    String agencyCode;
    String groupCode;
    String groupName;
    DatePickerDialog datePickerDialog;

    String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private MagicalPermissions magicalPermissions;
    MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_image);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        agencyCode = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE);
        groupCode = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_TEMP);
        groupName = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_TEMP2);
        getSupportActionBar().setTitle("Ảnh " + groupName + " của " + agencyCode);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magicalCamera.takePhoto();
              //  magicalCamera.selectedPicture("my_header_name");
            }
        });
        createDateDialog();
        decorImages = new ArrayList<>();
        adapter = new DecorImageAdapter(this, decorImages);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("images", decorImages.get(position).getUrl());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "show image");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int showMonth = calendar.get(Calendar.MONTH) + 1;
        int showYear = calendar.get(Calendar.YEAR);
        int showDay = calendar.get(Calendar.DATE);

        makeRequest(showDay, showMonth, showYear);

        createCamera();

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


        magicalCamera = new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);


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

    private void createDateDialog() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePickerDialog = new DatePickerDialog(
                DecorImageActivity.this, DecorImageActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

    }

    private void makeRequest(int day, int month, int year) {

        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        DecorImageSend info = new DecorImageSend();
        info.setToken(token);
        info.setUser(user);
        info.setYear(year);
        info.setDay(day);
        info.setMonth(month);
        info.setAgency(agencyCode);
        info.setGroup(groupCode);

        Call<List<DecorImage>> call = apiInterface().getDecorImages(info);
        call.enqueue(new Callback<List<DecorImage>>() {
            @Override
            public void onResponse(Call<List<DecorImage>> call, Response<List<DecorImage>> response) {
                hidepDialog();

                if (response.body() != null) {
                    decorImages.clear();
                    decorImages.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DecorImage>> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.date_action:
                datePickerDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker,  int year, int month, int i2) {
        makeRequest(i2, month + 1, year);
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
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),timeStamp, "NONGDUOCHAI", MagicalCamera.JPEG, true);

        uploadImage(path);

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

        RequestBody agency =
                RequestBody.create(
                        MediaType.parse("text/plain"), agencyCode);

        RequestBody group =
                RequestBody.create(
                        MediaType.parse("text/plain"), groupCode);

        RequestBody lat =
                RequestBody.create(
                        MediaType.parse("text/plain"), String.valueOf(getLat()));

        RequestBody lng =
                RequestBody.create(
                        MediaType.parse("text/plain"), String.valueOf(getLng()));


        Call<ResultInfo> call = apiInterfaceUpload().uploadImage(body, user, token, extension, agency, group, lat, lng);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                if (response.body()!= null) {
                    if (response.body().getId().equals("1")) {
                        Date date = new Date();
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        int showMonth = calendar.get(Calendar.MONTH) + 1;
                        int showYear = calendar.get(Calendar.YEAR);
                        int showDay = calendar.get(Calendar.DATE);

                        makeRequest(showDay, showMonth, showYear);
                    } else {
                        Toast.makeText(DecorImageActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });

    }
}
