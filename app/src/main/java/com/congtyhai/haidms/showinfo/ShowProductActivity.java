package com.congtyhai.haidms.showinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.congtyhai.adapter.ProductShowAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductActivity extends BaseActivity {

    private List<ProductCodeInfo> productCodeInfos ;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProductShowAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        createToolbar();
        ButterKnife.bind(this);

        productCodeInfos = new ArrayList<>();
        mAdapter = new ProductShowAdapter(productCodeInfos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        new ReadDataTask().execute();
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
            if (result == null || result.size() == 0) {
                makeRequest();
            } else {
                productCodeInfos = new ArrayList<>();
                for (ProductCodeInfo info : result) {
                    productCodeInfos.add(info);
                }

                mAdapter = new ProductShowAdapter(productCodeInfos);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }



        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_agency_show, menu);
        return true;
    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Call<ProductCodeInfo[]> call = apiInterface().getProduct(user, token);
        call.enqueue(new Callback<ProductCodeInfo[]>() {
            @Override
            public void onResponse(Call<ProductCodeInfo[]> call, Response<ProductCodeInfo[]> response) {
                if(response.body() != null) {
                    saveListProduct(response.body());
                    new ReadDataTask().execute();
                }
                hidepDialog();

            }

            @Override
            public void onFailure(Call<ProductCodeInfo[]> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refesh_action:
                makeRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
