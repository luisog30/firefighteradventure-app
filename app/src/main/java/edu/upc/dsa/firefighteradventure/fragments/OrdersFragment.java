package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.adapters.OrdersAdapter;
import edu.upc.dsa.firefighteradventure.adapters.ShopAdapter;
import edu.upc.dsa.firefighteradventure.models.Game;
import edu.upc.dsa.firefighteradventure.models.Inventory;
import edu.upc.dsa.firefighteradventure.models.Orders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OrdersFragment extends Fragment {

    private MainActivity mainActivity;

    private Button btnBackOrders;
    private RecyclerView rvOrders;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView tvNoOrders;

    private View view;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(true);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        btnBackOrders = view.findViewById(R.id.btnBackOrders);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
        btnBackOrders.setOnClickListener(this::btnBackOrdersClick);

        rvOrders = view.findViewById(R.id.rvOrders);
        getUserOrders();

    }


    public void getUserOrders() {

        mainActivity.setLoadingData(true);

        Call<List<Orders>> userOrdersCall = mainActivity.getUserService()
                .getUserOrdersByUsername(mainActivity.getSavedUsername());

        userOrdersCall.enqueue(new Callback<List<Orders>>() {

            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    List<Orders> ordersList = response.body();

                    tvNoOrders.setVisibility(View.VISIBLE);

                    if (ordersList.size() != 0) {

                        tvNoOrders.setVisibility(View.GONE);

                    }

                    Collections.sort(ordersList, new Comparator<Orders>() {
                        public int compare(Orders o1, Orders o2) {

                            String d1;
                            String d2;

                            String t1;
                            String t2;

                            d1 = o1.getOrderDate();
                            t1 = o1.getOrderTime();

                            d2 = o2.getOrderDate();
                            t2 = o2.getOrderTime();

                            Date date1 = Calendar.getInstance().getTime();
                            Date date2 = Calendar.getInstance().getTime();
                            Date time1 = Calendar.getInstance().getTime();
                            Date time2 = Calendar.getInstance().getTime();

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format =
                                    new SimpleDateFormat("dd/MM/yyyy");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 =
                                    new SimpleDateFormat("HH:mm");
                            try {
                                date1 = format.parse(d1);
                                date2 = format.parse(d2);

                                time1 = format2.parse(t1);
                                time2 = format2.parse(t2);
                            } catch (ParseException e) {

                            }

                            if (date1.compareTo(date2) == 0) {

                                return time2.compareTo(time1);

                            } else {

                                return date2.compareTo(date1) * 1000000;

                            }

                        }
                    });

                    rvOrders.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getContext());
                    rvOrders.setLayoutManager(layoutManager);

                    mAdapter = new OrdersAdapter(ordersList, getThis());
                    rvOrders.setAdapter(mAdapter);

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public void btnBackOrdersClick(android.view.View u) {
        mainActivity.goBack();

    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public OrdersFragment getThis() {
        return this;
    }

    public TextView getTvNoOrders() {
        return tvNoOrders;
    }
}