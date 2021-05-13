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
import edu.upc.dsa.firefighteradventure.adapters.ShopAdapter;
import edu.upc.dsa.firefighteradventure.models.Game;
import edu.upc.dsa.firefighteradventure.models.Inventory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShopFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    private Button btnBackShop;
    private TextView tvCoinsShop;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView rvShopItems;

    private int id_game;

    private Game game;

    private int coins;

    private List<Inventory> inventory;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(true);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        this.id_game = getArguments().getInt("id_game");

        btnBackShop = view.findViewById(R.id.btnBackShop);
        rvShopItems = view.findViewById(R.id.rvShopItems);
        tvCoinsShop = view.findViewById(R.id.tvCoinsShop);

        btnBackShop.setOnClickListener(this::btnBackShopClick);

        getGameInventory();

    }

    public void btnBackShopClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void getGameInventory() {

        mainActivity.setLoadingData(true);


        Call<List<Inventory>> gameInventoryCall = mainActivity.getGameService()
                .getGameInventory(this.id_game);

        gameInventoryCall.enqueue(new Callback<List<Inventory>>() {

            @Override
            public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    List<Inventory> inventory = response.body();

                    setInventory(inventory);

                    rvShopItems.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getContext());
                    rvShopItems.setLayoutManager(layoutManager);

                    mAdapter = new ShopAdapter(mainActivity.getShopItems(), getThis());
                    rvShopItems.setAdapter(mAdapter);

                    loadGame();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<Inventory>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    public int getQuantityInventory(int id_item) {

        int count = 0;

        for (Inventory i : getInventory()) {

            if (i.getId_item() == id_item) {
                count+=i.getQuantity();
            }

        }

        return count;

    }


    public void loadGame() {

        mainActivity.setLoadingData(true);

        Call<Game> resp = mainActivity.getGameService().getGameById(id_game);

        resp.enqueue(new Callback<Game>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:

                        Game resp = response.body();

                        updateCoins(resp.getCoins());
                        break;

                    case 404:
                        Toast.makeText(getContext(), R.string.game_does_not_exist_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }

    @SuppressLint("SetTextI18n")
    public void updateCoins(int coins) {

        setCoins(coins);
        tvCoinsShop.setText(getString(R.string.coins_string) + ": " + coins);

    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public ShopFragment getThis() {
        return this;
    }

    public int getId_game() {
        return id_game;
    }

    public void setId_game(int id_game) {
        this.id_game = id_game;
    }
}