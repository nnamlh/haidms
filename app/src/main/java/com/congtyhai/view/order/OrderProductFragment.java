package com.congtyhai.view.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.congtyhai.adapter.YourOrderProductAdapter;
import com.congtyhai.dms.R;
import com.congtyhai.dms.manageorders.YourOrderProductActivity;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.model.api.order.YourOrderShowResult;
import com.congtyhai.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderProductFragment extends Fragment {

    YourOrderProductAdapter adapter;

    List<OrderProductResult> orderProducts;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public YourOrderProductActivity activity;

    public void setYourACtivity(YourOrderProductActivity activity){
        this.activity = activity;
    }

    public void setData(List<OrderProductResult> dataes){
        orderProducts.clear();
        orderProducts.addAll(dataes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_product, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        orderProducts = new ArrayList<>();
        adapter = new YourOrderProductAdapter(orderProducts, activity, this);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void sendDelivery(int quantity, int position) {
        OrderProductResult info = orderProducts.get(position);

        activity.updateDelivery(position, quantity, info);

    }

    public void refeshUpdate(int position, int quantity) {
        OrderProductResult info = orderProducts.get(position);
        info.setQuantityFinish(quantity);

        adapter.notifyDataSetChanged();
    }

}
