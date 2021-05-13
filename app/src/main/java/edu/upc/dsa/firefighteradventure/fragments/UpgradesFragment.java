package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.Game;
import edu.upc.dsa.firefighteradventure.models.UpdateParameterRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpgradesFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackUpgrades;

    private Button btnUpgradeAttack;
    private Button btnUpgradeDefense;
    private Button btnUpgradeHealing;
    private Button btnUpgradeSpeed;

    private ProgressBar pbAttackUpgrades;
    private ProgressBar pbSpeedUpgrades;
    private ProgressBar pbHealingUpgrades;
    private ProgressBar pbDefenseUpgrades;

    private TextView tvAttackUpgrades;
    private TextView tvSpeedUpgrades;
    private TextView tvHealingUpgrades;
    private TextView tvDefenseUpgrades;

    private TextView tvCoinsUpgrades;

    private int coins;

    private int id_game;

    Game game;

    public UpgradesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upgrades, container, false);
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

        btnBackUpgrades = view.findViewById(R.id.btnBackUpgrades);
        btnUpgradeAttack = view.findViewById(R.id.btnUpgradeAttack);
        btnUpgradeDefense = view.findViewById(R.id.btnUpgradeDefense);
        btnUpgradeHealing = view.findViewById(R.id.btnUpgradeHealing);
        btnUpgradeSpeed = view.findViewById(R.id.btnUpgradeSpeed);

        pbAttackUpgrades = view.findViewById(R.id.pbAttackUpgrades);
        pbSpeedUpgrades = view.findViewById(R.id.pbSpeedUpgrades);
        pbHealingUpgrades = view.findViewById(R.id.pbHealingUpgrades);
        pbDefenseUpgrades = view.findViewById(R.id.pbDefenseUpgrades);

        tvAttackUpgrades = view.findViewById(R.id.tvAttackUpgrades);
        tvSpeedUpgrades = view.findViewById(R.id.tvSpeedUpgrades);
        tvHealingUpgrades = view.findViewById(R.id.tvHealingUpgrades);
        tvDefenseUpgrades = view.findViewById(R.id.tvDefenseUpgrades);

        btnUpgradeAttack.setOnClickListener(this::btnUpgradeAttack);
        btnUpgradeDefense.setOnClickListener(this::btnUpgradeDefense);
        btnUpgradeHealing.setOnClickListener(this::btnUpgradeHealing);
        btnUpgradeSpeed.setOnClickListener(this::btnUpgradeSpeed);

        btnBackUpgrades.setOnClickListener(this::btnBackUpgradesClick);

        tvCoinsUpgrades = view.findViewById(R.id.tvCoinsUpgrades);

        updateCoins(getArguments().getInt("coins"));
        id_game = getArguments().getInt("id_game");

        loadGame();

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

                        setGame(resp);

                        updateCoins(game.getCoins());

                        tvAttackUpgrades.setText(mainActivity.getString(R.string.attack_string)
                                + ": " + game.getAttack() + "/" + mainActivity.getGameSettings()
                                .getMax_attack());

                        pbAttackUpgrades.setProgress(100 * game.getAttack()
                                / mainActivity.getGameSettings().getMax_attack());

                        tvHealingUpgrades.setText(mainActivity.getString(R.string.healing_string)
                                + ": " + game.getHealing() + "/" + mainActivity.getGameSettings()
                                .getMax_healing());

                        pbHealingUpgrades.setProgress(100 * game.getHealing()
                                / mainActivity.getGameSettings().getMax_healing());

                        tvDefenseUpgrades.setText(mainActivity.getString(R.string.defense_string)
                                + ": " + game.getDefense() + "/" + mainActivity.getGameSettings()
                                .getMax_defense());

                        pbDefenseUpgrades.setProgress(100 * game.getDefense()
                                / mainActivity.getGameSettings().getMax_defense());

                        tvSpeedUpgrades.setText(mainActivity.getString(R.string.speed_string) + ": "
                                + game.getSpeed() + "/" + mainActivity.getGameSettings()
                                .getMax_speed());

                        pbSpeedUpgrades.setProgress(100 * game.getSpeed()
                                / mainActivity.getGameSettings().getMax_speed());

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
        tvCoinsUpgrades.setText(getString(R.string.coins_string) + ": " + coins);

    }

    public void btnBackUpgradesClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void btnUpgradeAttack(android.view.View u) {

        buy("attack");

    }

    public void btnUpgradeSpeed(android.view.View u) {

        buy("speed");

    }

    public void btnUpgradeDefense(android.view.View u) {

        buy("defense");

    }

    public void btnUpgradeHealing(android.view.View u) {

        buy("healing");

    }

    public void buy(String upgrade) {

        mainActivity.setLoadingData(true);

        Call<ResponseBody> resp = mainActivity.getShopService().
                buyUpgrade(upgrade, id_game);

        resp.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:
                        loadGame();
                        break;

                    case 404:
                        Toast.makeText(getContext(), R.string.game_does_not_exist_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    case 701:
                        Toast.makeText(getContext(), R.string.not_enough_coins_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }


    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}