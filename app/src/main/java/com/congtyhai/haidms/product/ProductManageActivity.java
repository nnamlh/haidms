package com.congtyhai.haidms.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.congtyhai.adapter.GeneralAdapter;
import com.congtyhai.adapter.ProductManageAdapter;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.Realm.DHistoryProductScan;
import com.congtyhai.model.api.CheckLocationRequest;
import com.congtyhai.model.api.GeneralInfo;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.ResultUpdate;
import com.congtyhai.model.api.StaffHelpRequest;
import com.congtyhai.model.api.UpdateProductInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.RealmController;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductManageActivity extends BaseActivity {

    @BindView(R.id.btnsend)
    Button btnSend;
    @BindView(R.id.btnscan)
    Button btnScan;
    @BindView(R.id.listproduct)
    ListView listView;
    ProductManageAdapter adapter;

    GeneralAdapter adapterResult;

    @BindView(R.id.txtcount)
    TextView txtCount;

    @BindView(R.id.edit_input)
    EditText editText;
    @BindView(R.id.edit_receive)
    EditText eReceiver;

    boolean isReset = true;

    @BindView(R.id.layout_receiver)
    View lReceiver;

    String status;

    @BindView(R.id.layout_agency)
    View lAgency;
    @BindView(R.id.edit_agency)
    EditText txtAgency;

    String title;

    String companyCode = "89352433";

    List<ProductCodeInfo> productCodeInfos;


    int CHOOSE_AGENCY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();

        status = intent.getStringExtra("STATUS");

        if (status.equals(HAIRes.getInstance().PRODUCT_IMPORT)) {
            title = "Nhập kho";
            eReceiver.setText("");
        } else if (status.equals(HAIRes.getInstance().PRODUCT_EXPORT)) {
            title = "Xuất kho";
            lReceiver.setVisibility(View.VISIBLE);
        } else if (status.equals(HAIRes.getInstance().PRODUCT_HELP_SCAN)) {
            title = "Nhập giúp";
            lAgency.setVisibility(View.VISIBLE);
        } else if (status.equals(HAIRes.getInstance().PRODUCT_TRANSPORT)) {
            title = "Điều kho";
            lReceiver.setVisibility(View.VISIBLE);
        } else {
            onBackPressed();
        }
        getSupportActionBar().setTitle(title);


        productCodeInfos = new ArrayList<>();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isReset)
                    resetList();

                Intent intent = new Intent(ProductManageActivity.this, ProductScanActivity.class);
                intent.putExtra("STATUS", status);
                startActivity(intent);

            }
        });

        HAIRes.getInstance().resetListProduct();

        adapter = new ProductManageAdapter(this, HAIRes.getInstance().getLIST_PRODUCT());


        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);
                return false;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HAIRes.getInstance().countListProduct() == 0) {
                    commons.showAlertInfo(ProductManageActivity.this, "Cảnh báo", "Nhâp danh sách sản phẩm trước khi cập nhật.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                } else
                    showAlertUpdate();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.input_text || id == EditorInfo.IME_NULL) {
                    addProduct();

                    return true;
                }
                return false;
            }
        });

        eReceiver.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                + android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

       new ReadDataTask().execute();

    }

    public void addProductClick(View view) {
        addProduct();
    }

    private void addProduct() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            commons.showAlertInfo(ProductManageActivity.this, "Cảnh báo", "Nhập mã sản phẩm.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {

            if (editText.getText().length() < 9) {
                commons.showAlertInfo(ProductManageActivity.this, "Cảnh báo", "Không hợp lệ.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            } else {
                if (!isReset)
                    resetList();

                HAIRes.getInstance().addListProduct(companyCode + editText.getText().toString());
                adapter.notifyDataSetChanged();
                txtCount.setText("Tổng số lượng: " + HAIRes.getInstance().countListProduct());
                editText.setText("");
            }
        }
    }

    private String chooseStt() {
        if (status.equals(HAIRes.getInstance().PRODUCT_TRANSPORT)) {
            return HAIRes.getInstance().PRODUCT_EXPORT;
        }
        return status;
    }

    public void findClick(View view) {

    }

    private void sendInfo() {

        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        UpdateProductInfo info = new UpdateProductInfo();

        info.setStatus(chooseStt());
        info.setUser(user);
        info.setToken(token);
        info.setProducts(HAIRes.getInstance().toProductArrays());
        info.setReceiver(eReceiver.getText().toString());

        Call<ResultUpdate> call = apiInterface().updateProduct(info);

        call.enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call,
                                   final Response<ResultUpdate> response) {
                hidepDialog();
                if ("1".equals(response.body().getId())) {
                    isReset = false;
                    HAIRes.getInstance().resetListProduct();

                    int countErorr = 0;
                    int countSucces = 0;
                    for (GeneralInfo item : response.body().getProducts()) {
                        if (item.getSuccess() == 1)
                            countSucces++;
                        else
                            countErorr++;
                    }
                    String msg = "Tổng công : " + response.body().getProducts().size() + "\n" + "Thành công: " + countSucces + "\n" + "Lỗi: " + countErorr;
                    commons.showAlertInfo( ProductManageActivity.this, "Thông báo", msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            refeshUpdateList(response.body().getProducts());
                        }
                    });

                    saveHistory(1, response.body().getProducts(), 1, countSucces, countErorr);

                } else {
                    commons.showAlertInfo( ProductManageActivity.this, "Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                hidepDialog();
                commons.showAlertInfo( ProductManageActivity.this,"Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

    private void sendStaffHelp(final int near) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        StaffHelpRequest info = new StaffHelpRequest(user,token, HAIRes.getInstance().toProductArrays(),getLat(), getLng(), txtAgency.getText().toString(), near);

        Call<ResultUpdate> call = apiInterface().updateAgencyimport(info);

        call.enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call,
                                   final Response<ResultUpdate> response) {
                hidepDialog();
                if ("1".equals(response.body().getId())) {
                    isReset = false;
                    HAIRes.getInstance().resetListProduct();
                    int countErorr = 0;
                    int countSucces = 0;
                    for (GeneralInfo item : response.body().getProducts()) {
                        if (item.getSuccess() == 1)
                            countSucces++;
                        else
                            countErorr++;
                    }
                    String msg = "Tổng công : " + response.body().getProducts().size() + "\n" + "Thành công: " + countSucces + "\n" + "Lỗi: " + countErorr;
                    commons.showAlertInfo( ProductManageActivity.this,"Thông báo", msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            refeshUpdateList(response.body().getProducts());
                        }
                    });

                    saveHistory(1, response.body().getProducts(), near, countSucces, countErorr);

                } else {
                    commons.showAlertInfo( ProductManageActivity.this,"Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                hidepDialog();
                commons.showAlertInfo( ProductManageActivity.this,"Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_AGENCY) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("code");
                txtAgency.setText(code);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void saveHistory(final int isUpdate, final List<GeneralInfo> result, final int isNear, final int countSucess, final int countFail) {
        final String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm",
                Locale.getDefault()).format(new Date());
        final Gson gson = new Gson();
        realmControl.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DHistoryProductScan historyProductScan = realm.createObject(DHistoryProductScan.class, RealmController.getInstance().getNextKey(DHistoryProductScan.class));
                historyProductScan.setAgency(txtAgency.getText().toString());
                historyProductScan.setReceive(eReceiver.getText().toString());
                historyProductScan.setStatus(chooseStt());
                historyProductScan.setScreen(status);
                historyProductScan.setTime(timeStamp);
                historyProductScan.setCountSuccess("" + countSucess);
                historyProductScan.setQuantity(result.size() + "");
                historyProductScan.setCountFail("" + countFail);
                historyProductScan.setProductResult(gson.toJson(result));
                historyProductScan.setProduct(gson.toJson(HAIRes.getInstance().getLIST_PRODUCT()));
            }
        });
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<ProductCodeInfo>> {
        protected List<ProductCodeInfo> doInBackground(String... urls) {

            List<ProductCodeInfo> data = new ArrayList<>();
            try {

                data = getListProduct();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<ProductCodeInfo> result) {
            hidepDialog();
            HAIRes.getInstance().clearProductCodeMap();
            if (result == null || result.size() == 0) {
                productCodeInfos = new ArrayList<>();
            } else {
                productCodeInfos.addAll(result);

                for(ProductCodeInfo info: productCodeInfos) {
                    HAIRes.getInstance().addProductCodeMap(info);
                }

            }


        }
    }

    private void refeshUpdateList(List<GeneralInfo> products) {
        adapterResult = new GeneralAdapter(ProductManageActivity.this, products);

        listView.setAdapter(adapterResult);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        txtCount.setText("TỔNG SỐ LƯỢNG : " + products.size());
    }

    private void sendCheckDistance() {

        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        CheckLocationRequest info = new CheckLocationRequest(user, token, txtAgency.getText().toString(), getLat(), getLng());

        Call<ResultInfo> call = apiInterface().checkLocationDistance(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   final Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        // thanh cong
                        sendStaffHelp(1);

                    } else if ("2".equals(response.body().getId())) {
                        // ngoai dai ly
                        commons.showAlertCancel(ProductManageActivity.this, "Cảnh báo", "Chúng tôi đang định vị bạn ở ngoài đại lý, bạn đồng ý tiếp tục cập nhật ?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendStaffHelp(2);
                            }
                        });
                    } else {
                        commons.showAlertInfo(ProductManageActivity.this,"Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }

                } else {
                    commons.showAlertInfo(ProductManageActivity.this,"Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.showAlertInfo(ProductManageActivity.this,"Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

    private void showAlertUpdate() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductManageActivity.this);

        dialog.setTitle("Cập nhật")
                .setMessage("Cập nhật sản phẩm.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status.equals(HAIRes.getInstance().PRODUCT_HELP_SCAN)) {

                                if (TextUtils.isEmpty(txtAgency.getText().toString())) {
                                    Toast.makeText(ProductManageActivity.this, "Nhập mã đại lý", Toast.LENGTH_LONG).show();
                                } else {
                                    sendCheckDistance();
                                }

                        } else {
                            sendInfo();
                        }
                    }
                }).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void findAgencyClick(View view) {
        Intent intentSend = commons.createIntent(ProductManageActivity.this, ShowAgencyActivity.class);
        intentSend.putExtra(HAIRes.getInstance().KEY_INTENT_ACTION, "chooseagency");
        startActivityForResult(intentSend, CHOOSE_AGENCY);
    }

    private void showAlert(final int pos) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductManageActivity.this);
        String code = HAIRes.getInstance().getLIST_PRODUCT().get(pos);
        dialog.setTitle("Thông báo")
                .setMessage("Xóa mã : " + code)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        HAIRes.getInstance().getLIST_PRODUCT().remove(pos);
                        adapter.notifyDataSetChanged();
                        txtCount.setText("TỔNG SỐ LƯỢNG : " + HAIRes.getInstance().countListProduct());
                    }
                }).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        txtCount.setText("Tổng số lượng : " + HAIRes.getInstance().countListProduct());
    }

    @Override
    public void onBackPressed() {

        commons.showAlertCancel(ProductManageActivity.this, "Cảnh báo", "Bạn thoát màn hình này nếu chưa cập nhật dữ liệu sẽ không được lưu lại !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProductManageActivity.super.onBackPressed();
            }
        });


    }

    private void resetList() {
        isReset = true;
        HAIRes.getInstance().resetListProduct();
        adapter = new ProductManageAdapter(this, HAIRes.getInstance().getLIST_PRODUCT());

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);
                return false;
            }
        });

        listView.setAdapter(adapter);

        txtCount.setText("Tổng số lượng : " + HAIRes.getInstance().countListProduct());
    }

}
