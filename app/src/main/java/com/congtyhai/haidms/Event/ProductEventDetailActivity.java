package com.congtyhai.haidms.Event;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import com.congtyhai.adapter.EventProductAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.EventProduct;
import com.congtyhai.util.HAIRes;
import java.util.ArrayList;
import java.util.List;

public class ProductEventDetailActivity extends BaseActivity {

    private EventProductAdapter eventProductAdapter;
    private List<EventProduct> eventProducts;
    private ListView listProduct;

    private EditText eSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_event_detail);
        createToolbar();
        eSearch = (EditText) findViewById(R.id.inputSearch);

        eventProducts = new ArrayList<>();
        eventProductAdapter = new EventProductAdapter(ProductEventDetailActivity.this, eventProducts);
        listProduct = (ListView) findViewById(R.id.listproduct);

        listProduct.setAdapter(eventProductAdapter);

        initList();

        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                eventProducts.clear();
                for (EventProduct item : HAIRes.getInstance().getListProductEvent()) {
                    if (item.getName().toLowerCase().contains(charSequence)) {
                        eventProducts.add(item);
                    }
                }
                eventProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initList() {
        for (EventProduct item : HAIRes.getInstance().getListProductEvent()) {
            eventProducts.add(item);
        }

        eventProductAdapter.notifyDataSetChanged();
    }
}
