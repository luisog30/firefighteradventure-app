package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ShopUpgradesFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackShopUpgrades;
    private Button btnGotoShop;
    private Button btnGotoUpgrades;

    public ShopUpgradesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop_upgrades, container, false);
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

        btnBackShopUpgrades = view.findViewById(R.id.btnBackShopUpgrades);
        btnGotoShop = view.findViewById(R.id.btnGotoShop);
        btnGotoUpgrades = view.findViewById(R.id.btnGotoUpgrades);


        btnBackShopUpgrades.setOnClickListener(this::btnBackShopUpgradesClick);
        btnGotoShop.setOnClickListener(this::btnGotoShopClick);
        btnGotoUpgrades.setOnClickListener(this::btnGotoUpgradesClick);

    }


    public void btnBackShopUpgradesClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void btnGotoShopClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id
                        .action_shopUpgradesFragment_to_shopFragment
                , getArguments());

    }

    public void btnGotoUpgradesClick(android.view.View u) {


        Navigation.findNavController(view).navigate(R.id
                        .action_shopUpgradesFragment_to_upgradesFragment
                , getArguments());

    }

}