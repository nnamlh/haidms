package com.congtyhai.dms.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.congtyhai.adapter.ProductManageAdapter;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.util.HAIRes;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ProductScanActivity extends BaseActivity implements ZBarScannerView.ResultHandler {
    private static final String TAG = ProductScanActivity.class.getSimpleName();

    private ZBarScannerView mScannerView;
    private ListView listView;
    ProductManageAdapter adapter;
    String status;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_scan);
        createToolbar();

        Intent intent = getIntent();

        status = intent.getStringExtra("STATUS");

        if (status.equals(HAIRes.getInstance().PRODUCT_IMPORT)) {
            title = "Nhập kho";
        } else if (status.equals(HAIRes.getInstance().PRODUCT_EXPORT)) {
            title = "Xuất kho";
        } else if (status.equals(HAIRes.getInstance().PRODUCT_HELP_SCAN)) {
            title = "Nhập giúp";
        } else if (status.equals(HAIRes.getInstance().PRODUCT_TRANSPORT)){
            title = "Điều kho";
        }
        else {
            onBackPressed();
        }

        getSupportActionBar().setTitle(title);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.barcode_scanner);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);
        setupFormats();

        listView = (ListView) findViewById(R.id.listproduct);
        adapter = new ProductManageAdapter(this, HAIRes.getInstance().getLIST_PRODUCT());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);
                return false;
            }
        });
    }

    private void showAlert(final int pos) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductScanActivity.this);
        String code = HAIRes.getInstance().getLIST_PRODUCT().get(pos);
        dialog.setTitle("Thông báo")
                .setMessage("Xóa mã : " + code)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        HAIRes.getInstance().getLIST_PRODUCT().remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();

        formats.add(new BarcodeFormat(39, "CODE39"));
        formats.add(new BarcodeFormat(128, "CODE128"));
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult.getContents() == null || HAIRes.getInstance().getLIST_PRODUCT().contains(rawResult.getContents())) {
            Toast.makeText(getApplicationContext(), "Đã quét.", Toast.LENGTH_SHORT).show();
        } else if (rawResult.getContents().length() < 17) {
            Toast.makeText(getApplicationContext(), "Mã không hợp lệ.", Toast.LENGTH_SHORT).show();
        }
        else {
            HAIRes.getInstance().addListProduct(rawResult.getContents());
            adapter.notifyDataSetChanged();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ProductScanActivity.this);
            }
        }, 500);
    }

}
