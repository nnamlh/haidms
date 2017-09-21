package com.congtyhai.haidms.product;

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
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.GeneralInfo;
import com.congtyhai.util.HAIRes;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


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

      //  new ReadDataTask().execute();

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

    }

    private void sendStaffHelp(final int near) {

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


    }

    private void showAlertUpdate() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductManageActivity.this);

        dialog.setTitle("Cập nhật")
                .setMessage("Cập nhật sản phẩm.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status.equals(HAIRes.getInstance().PRODUCT_HELP_SCAN)) {
                            if (checkLocation()) {
                                if (TextUtils.isEmpty(txtAgency.getText().toString())) {
                                    Toast.makeText(ProductManageActivity.this, "Nhập mã đại lý", Toast.LENGTH_LONG).show();
                                } else {
                                    sendCheckDistance();
                                }
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


    private class ReadDataTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            try {
                  getListProduct();

            } catch (Exception e) {

            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(String result) {
            hidepDialog();
        }
    }
}
