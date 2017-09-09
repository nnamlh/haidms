package com.congtyhai.haidms.showinfo;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.congtyhai.adapter.ProductShowAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.GroupResultInfo;
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

    private List<ProductCodeInfo> productCodeInfos;

    private List<ProductCodeInfo> productCodeInfosTemp;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProductShowAdapter mAdapter;

    private List<String> groupName;
    private List<String> groupCode;

    AlertDialog.Builder builderSingle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        createToolbar();
        ButterKnife.bind(this);

        groupCode = new ArrayList<>();
        groupName = new ArrayList<>();
        productCodeInfosTemp = new ArrayList<>();
        productCodeInfos = new ArrayList<>();
        mAdapter = new ProductShowAdapter(productCodeInfos, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = commons.createIntent(ShowProductActivity.this, ShowProductDetailActivity.class);
                ProductCodeInfo info = productCodeInfos.get(position);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, info.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        new ReadDataTask().execute();
    }

    private void createDialogGroup() {
        builderSingle = new AlertDialog.Builder(ShowProductActivity.this);
        builderSingle.setIcon(R.mipmap.ic_logo);
        builderSingle.setTitle("Chọn nhóm:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowProductActivity.this, android.R.layout.select_dialog_singlechoice, groupName);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = groupCode.get(which);
                productCodeInfos.clear();

                for(ProductCodeInfo info: productCodeInfosTemp) {
                    if (info.getCode().equals(code)){
                        productCodeInfos.add(info);
                    }
                }

                mAdapter.notifyDataSetChanged();

            }
        });

    }

    private class ReadGroupTask extends AsyncTask<String, Integer, List<GroupResultInfo>> {

        @Override
        protected List<GroupResultInfo> doInBackground(String... strings) {
            List<GroupResultInfo> data = new ArrayList<>();
            try {

                data = getListProductGroup();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<GroupResultInfo> result) {


            for (GroupResultInfo info : result) {
                groupName.add(info.getName());
                groupCode.add(info.getId());
            }
            createDialogGroup();
            hidepDialog();

        }
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

                productCodeInfos.addAll(result);
                productCodeInfosTemp.addAll(result);

                mAdapter.notifyDataSetChanged();

                new ReadGroupTask().execute();

            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_show, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.find_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return false;
            }
        });
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
                if (response.body() != null) {
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
            case R.id.filter_all_action:
                productCodeInfos.clear();
                productCodeInfos.addAll(productCodeInfosTemp);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_forcus_action:
                productCodeInfos.clear();
                for (ProductCodeInfo info : productCodeInfosTemp) {
                    if (info.getIsForcus() == 1) {
                        productCodeInfos.add(info);
                    }
                }
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_new_action:
                productCodeInfos.clear();
                for (ProductCodeInfo info : productCodeInfosTemp) {
                    if (info.getIsNew() == 1) {
                        productCodeInfos.add(info);
                    }
                }
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_group_action:
                builderSingle.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleSearch(String query) {

        productCodeInfos.clear();

        for (ProductCodeInfo info : productCodeInfosTemp) {
            if (info.getName().toLowerCase().contains(query.toLowerCase())) {
                productCodeInfos.add(info);
            }
        }
        // mAdapter = new ProductShowAdapter(productCodeInfos, ShowProductActivity.this);
        // recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
