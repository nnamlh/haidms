package com.congtyhai.haidms.Agency;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.btnAdd)
    Button btnAdd;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.ec1)
    EditText eC1;

    @BindView(R.id.erank)
    Spinner eRank;

    @BindView(R.id.egroup)
    EditText eGroup;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    int C1_CODE = 2;

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agency);
        createToolbar();
        ButterKnife.bind(this);
        createLocation();
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.showAlertCancel(AddAgencyActivity.this, "Cảnh báo", "Bạn muốn thêm khách hàng mới ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });

        initERank();

    }

    private void initERank() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.agency_rank, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eRank.setAdapter(adapter);
    }

    private void makeRequest() {

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

           if (result != null ) {
               eAddress.setText(result.getAddressLine(0));
               eProvince.setText(result.getAdminArea());
               eDistrict.setText(result.getSubAdminArea());
               eWard.setText(result.getSubLocality());
               eCountry.setText(result.getCountryName());
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
        }

        if (requestCode == C1_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                eC1.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
