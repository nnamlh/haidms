package com.congtyhai.dms.checkin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import com.congtyhai.adapter.DecorFolderAdapter;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.DecorFolder;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecorActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    DecorFolderAdapter adapter;

    List<DecorFolder> decorFolders;

    String agencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        agencyCode = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_TEMP);
        getSupportActionBar().setTitle("Đang ghé thăm: " + agencyCode);
        decorFolders = new ArrayList<>();
        adapter = new DecorFolderAdapter(decorFolders);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DecorFolder folder = decorFolders.get(position);
                Intent intentImage = commons.createIntent(DecorActivity.this, DecorImageActivity.class);
                intentImage.putExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE, agencyCode);
                intentImage.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, folder.getCode());
                intentImage.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP2, folder.getName());
                startActivity(intentImage);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        makeRequest();
    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        Call<List<DecorFolder>> call = apiInterface().getDecorFolder(user, token);
        call.enqueue(new Callback<List<DecorFolder>>() {
            @Override
            public void onResponse(Call<List<DecorFolder>> call, Response<List<DecorFolder>> response) {
                hidepDialog();
                if(response.body() != null) {
                    decorFolders.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DecorFolder>> call, Throwable t) {
                hidepDialog();
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
