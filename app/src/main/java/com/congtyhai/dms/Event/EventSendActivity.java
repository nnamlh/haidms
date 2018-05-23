package com.congtyhai.dms.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.CodeSendInfo;
import com.congtyhai.model.api.CodeSendResult;
import com.congtyhai.util.HAIRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventSendActivity extends BaseActivity {

    private ListView lv;

    // Listview Adapter
    private ArrayAdapter<String> adapter;

    //  private List<String> listCode;

    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_send);
        createToolbar();
        lv = (ListView) findViewById(R.id.list_view);
        editText = (EditText) findViewById(R.id.event_input);
        // listCode = new ArrayList<>();
        HAIRes.getInstance().resetEventCode();
        adapter = new ArrayAdapter<String>(this, R.layout.event_code_send_item, R.id.event_code, HAIRes.getInstance().getEVENTCODES());


        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int index, final long l) {

                commons.showAlertCancel(EventSendActivity.this, "Xóa mã", "Bạn đồng ý xóa ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HAIRes.getInstance().getEVENTCODES().remove(index);
                        adapter.notifyDataSetChanged();
                    }
                });

                return false;
            }
        });


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.input_text || id == EditorInfo.IME_NULL) {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        commons.showAlertInfo(EventSendActivity.this, "Cảnh báo", "Nhập mã thẻ.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else {
                        if (!HAIRes.getInstance().addToEventCode(editText.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Đã có trong danh sách.", Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                        editText.setText("");
                    }


                    return true;
                }
                return false;
            }
        });
    }


    public void addCodeClick(View view) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            commons.showAlertInfo(EventSendActivity.this, "Cảnh báo", "Nhập mã thẻ.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {

            if (editText.getText().length() < 17) {
                commons.showAlertInfo(EventSendActivity.this,"Cảnh báo", "Mã không hợp lệ.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            } else if (!HAIRes.getInstance().addToEventCode(editText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Đã có trong danh sách.", Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            editText.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }




    public void scanCodeClick(View view) {

        Intent intent = new Intent(EventSendActivity.this, ScanEventCodeActivity.class);
        startActivity(intent);
    }

    public void sendCodeClick(View view) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        String[] arrays = new String[HAIRes.getInstance().getEVENTCODES().size()];

        int i = 0;
        for (String item : HAIRes.getInstance().getEVENTCODES()) {
            arrays[i] = item;
            i++;
        }

        CodeSendInfo info = new CodeSendInfo(user, token, arrays);

        Call<CodeSendResult> call = apiInterface().sendCode(info);

        call.enqueue(new Callback<CodeSendResult>() {
            @Override
            public void onResponse(Call<CodeSendResult> call,
                                   Response<CodeSendResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        HAIRes.getInstance().resetEventCode();
                        adapter.notifyDataSetChanged();
                        HAIRes.getInstance().setEventCodeResult(response.body().getCodes());

                        Intent intent = new Intent(EventSendActivity.this, SendCodeResultActivity.class);
                        intent.putExtra("msg", response.body().getMsg());
                        startActivity(intent);

                    } else {
                        commons.showAlertInfo(EventSendActivity.this ,"Kết quả", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }

                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<CodeSendResult> call, Throwable t) {
                hidepDialog();
                commons.showAlertInfo(EventSendActivity.this ,"Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

}
