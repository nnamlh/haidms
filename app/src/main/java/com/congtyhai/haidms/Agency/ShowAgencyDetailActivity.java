package com.congtyhai.haidms.Agency;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.SubOwner;
import com.congtyhai.model.api.AgencyModifySend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAgencyDetailActivity extends BaseActivity {

    @BindView(R.id.ecode)
    EditText eCode;

    @BindView(R.id.estore)
    EditText eSotre;

    @BindView(R.id.edeputy)
    EditText eDeputy;


    @BindView(R.id.ec1)
    EditText eC1;

    @BindView(R.id.listc1)
    LinearLayout lC1;

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

    @BindView(R.id.erank)
    EditText eRank;

    @BindView(R.id.egroup)
    EditText eGroup;

    @BindView(R.id.etax)
    EditText eTax;
    @BindView(R.id.eward)
    EditText eWard;
    @BindView(R.id.ecountry)
    EditText eCountry;

    @BindView(R.id.imgAddress)
    ImageView imgLoatiom;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    double lat;
    double lng;
    Menu menu;

    @BindView(R.id.ebranch)
    EditText eBranch;

    @BindView(R.id.ec12)
    EditText eC12;

    @BindView(R.id.ec13)
    EditText eC13;

    @BindView(R.id.title)
    TextView txtTitle;

    boolean isUpdate = false;

    int SHOW_UPDATE_LOCATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_agency_detail);
        createToolbar();
        ButterKnife.bind(this);

        eCode.setText(HAIRes.getInstance().currentAgencySelect.getCode());
        eSotre.setText(HAIRes.getInstance().currentAgencySelect.getName());
        eDeputy.setText(HAIRes.getInstance().currentAgencySelect.getDeputy());
        eIdentityCard.setText(HAIRes.getInstance().currentAgencySelect.getIdentityCard());
        eBusinessLicene.setText(HAIRes.getInstance().currentAgencySelect.getBusinessLicense());
        ePhone.setText(HAIRes.getInstance().currentAgencySelect.getPhone());
        eAddress.setText(HAIRes.getInstance().currentAgencySelect.getAddress());
        eProvince.setText(HAIRes.getInstance().currentAgencySelect.getProvince());
        eDistrict.setText(HAIRes.getInstance().currentAgencySelect.getDistrict());
        eProvince.setText(HAIRes.getInstance().currentAgencySelect.getDistrict());
        eRank.setText(HAIRes.getInstance().currentAgencySelect.getRank());
        eGroup.setText(HAIRes.getInstance().currentAgencySelect.getGroup() + "");
        eTax.setText(HAIRes.getInstance().currentAgencySelect.getTaxCode());
        eWard.setText(HAIRes.getInstance().currentAgencySelect.getWard());
        eCountry.setText(HAIRes.getInstance().currentAgencySelect.getCountry());
        eBranch.setText(HAIRes.getInstance().currentAgencySelect.getHaibranch());


        if(HAIRes.getInstance().currentAgencySelect.getType().equals("CII")){
            txtTitle.setText("Đại lý cấp 2");
        } else {
            txtTitle.setText("Đại lý cấp 1");
            btnAdd.setVisibility(View.GONE);
            // cap 1 ko cap nhat
        }

        imgLoatiom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(ShowAgencyDetailActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {

                } catch (GooglePlayServicesNotAvailableException e) {

                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.showAlertCancel(ShowAgencyDetailActivity.this, "Cảnh báo", "Bạn muốn chỉnh sửa khách hàng ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeRequest(false);
                    }
                });
            }
        });

      if(HAIRes.getInstance().currentAgencySelect.getType().equals("CII")) {
          lC1.setVisibility(View.VISIBLE);
          List<SubOwner> c2C1s = HAIRes.getInstance().currentAgencySelect.getSubOwner();
          for(int i=0; i < c2C1s.size(); i++) {
              switch (i) {
                  case 0:
                      eC1.setText(c2C1s.get(i).getName() + " - " + c2C1s.get(i).getCode());
                      break;
                  case 1:
                      eC12.setText(c2C1s.get(i).getName() + " - " + c2C1s.get(i).getCode());
                      break;
                  case 2:
                      eC13.setText(c2C1s.get(i).getName() + " - " + c2C1s.get(i).getCode());
                      break;
              }
          }
      } else {
          lC1.setVisibility(View.GONE);
      }

    }

    private class ReadDataTask extends AsyncTask<String, Integer, Address> {
        protected Address doInBackground(String... urls) {

            final Geocoder geocoder = new Geocoder(ShowAgencyDetailActivity.this);

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

    private void makeRequest(final boolean isUpdateLocation) {
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        AgencyModifySend info = new AgencyModifySend();
        if (!checkRequire())
            return;

        showpDialog();
        info.setId(HAIRes.getInstance().currentAgencySelect.getId());
        info.setUser(user);
        info.setToken(token);

        info.setC1Id(eC1.getText().toString());
        info.setDeputy(eDeputy.getText().toString());
        info.setName(eSotre.getText().toString());
        info.setBusinessLicense(eBusinessLicene.getText().toString());
        info.setIdentityCard(eIdentityCard.getText().toString());
        info.setAddress(eAddress.getText().toString());
        info.setCountry(eCountry.getText().toString());
        info.setProvince(eProvince.getText().toString());
        info.setWard(eWard.getText().toString());
        info.setGroup(Integer.parseInt(eGroup.getText().toString()));
        info.setPhone(ePhone.getText().toString());
        info.setTaxCode(eTax.getText().toString());

        if (isUpdateLocation) {
            info.setLat(getLat());
            info.setLng(getLng());
        } else {
            info.setLat(0);
            info.setLng(0);
        }

        Call<ResultInfo> call = apiInterface().modifyAgencyC2(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body().getId().equals("1")) {
                    isUpdate = true;
                    commons.showAlertInfo(ShowAgencyDetailActivity.this, "Thông báo", "Đã cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isUpdateLocation) {
                                MenuItem menuItem = menu.findItem(R.id.location_action);
                                menuItem.setVisible(false);
                            }
                        }
                    });
                } else {
                    commons.showAlertInfo(ShowAgencyDetailActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.makeToast(ShowAgencyDetailActivity.this, "Lỗi đường truyền");
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
         /*
        if (TextUtils.isEmpty(ePhone.getText().toString())) {
            ePhone.setError("Không để trống số điện thoại");
            return false;
        }

        if (TextUtils.isEmpty(eGroup.getText().toString())) {
            eGroup.setError("Không để trống cụm khách hàng");
            return false;
        }


        if (TextUtils.isEmpty(eC1.getText().toString())) {
            eC1.setError("Không để trống cấp 1");
            return false;
        }

      */
        if (TextUtils.isEmpty(eAddress.getText().toString())) {
            eAddress.setError("Không để trống địa chỉ");
            return false;
        }

        return true;
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

        if (requestCode == SHOW_UPDATE_LOCATION) {
            if (resultCode == RESULT_OK) {
                MenuItem menuItem = menu.findItem(R.id.location_action);
                menuItem.setVisible(false);
                isUpdate = true;
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show_agency_detail, menu);
        this.menu = menu;

        if (HAIRes.getInstance().currentAgencySelect.getLng() != 0 && HAIRes.getInstance().currentAgencySelect.getLng() != 0) {
            MenuItem menuItem = menu.findItem(R.id.location_action);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.location_action:
                //  makeRequest(true);
                Intent intent = commons.createIntent(ShowAgencyDetailActivity.this, AgencyUpdateLocationActivity.class);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE, HAIRes.getInstance().currentAgencySelect.getId());
                startActivityForResult(intent, SHOW_UPDATE_LOCATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (isUpdate) {
            setResult(Activity.RESULT_OK, returnIntent);
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        finish();
    }

}
