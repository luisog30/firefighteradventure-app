package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.Credentials.LoginCredentials;
import edu.upc.dsa.firefighteradventure.models.GameSettings;
import edu.upc.dsa.firefighteradventure.models.Item;
import edu.upc.dsa.firefighteradventure.models.Map;
import edu.upc.dsa.firefighteradventure.models.UserSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(false);

        TimerTask mTimerTask = new TimerTask() {

            @Override
            public void run() {

                if (!mainActivity.isNetworkConnected()) {

                    Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);

                } else {

                    Call<UserSettings> userSettingsCall = mainActivity.getUserService()
                            .getUserSettings();

                    userSettingsCall.enqueue(new Callback<UserSettings>() {

                        @Override
                        public void onResponse(Call<UserSettings> call
                                , Response<UserSettings> response) {

                            if (response.code() == 200) {

                                UserSettings userSettings = response.body();

                                mainActivity.setUserSettings(userSettings);

                                getGameSettings();

                            } else {

                                Navigation.findNavController(view).navigate(R.id
                                        .connectionErrorFragment);

                            }

                        }

                        @Override
                        public void onFailure(Call<UserSettings> call, Throwable t) {

                            Navigation.findNavController(view).navigate(R.id
                                    .connectionErrorFragment);

                        }

                    });


                }

            }
        };

        Timer mTimer = new Timer();
        mTimer.schedule(mTimerTask, 3000);

    }

    public void getGameMaps() {

        Call<List<Map>> gameMapsCall = mainActivity.getGameService().getGameMaps();

        gameMapsCall.enqueue(new Callback<List<Map>>() {

            @Override
            public void onResponse(Call<List<Map>> call, Response<List<Map>> response) {

                if (response.code() == 200) {

                    List<Map> gameMaps = response.body();

                    mainActivity.setGameMaps(gameMaps);

                    getShopItems();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<Map>> call, Throwable t) {

                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }


    public void getShopItems() {

        Call<List<Item>> gameItemsCall = mainActivity.getShopService().getShopItems();

        gameItemsCall.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                if (response.code() == 200) {

                    List<Item> shopItems = response.body();

                    mainActivity.setShopItems(shopItems);

                    tryLogin();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }

    public void getGameSettings() {

        Call<GameSettings> gameSettingsCall = mainActivity.getGameService().getGameSettings();

        gameSettingsCall.enqueue(new Callback<GameSettings>() {

            @Override
            public void onResponse(Call<GameSettings> call, Response<GameSettings> response) {

                if (response.code() == 200) {

                    GameSettings gameSettings = response.body();

                    mainActivity.setGameSettings(gameSettings);

                    getGameMaps();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<GameSettings> call, Throwable t) {

                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }


    public void tryLogin() {

        String username = mainActivity.getSavedUsername();
        String password = mainActivity.getSavedPassword();

        if (username.equals("")) {

            Navigation.findNavController(view).navigate(R.id
                    .action_splashScreenFragment_to_loginRegisterFragment);

        } else {

            Call<ResponseBody> resp = mainActivity.getUserService()
                    .login(new LoginCredentials(username, password));

            resp.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {

                        Navigation.findNavController(view).navigate(R.id
                                .action_splashScreenFragment_to_mainMenuFragment);

                    } else {

                        switch (response.code()) {

                            case 250:
                                Toast.makeText(getContext(), R.string.user_not_exists_string
                                        , Toast.LENGTH_SHORT).show();
                                break;
                            case 601:
                                Toast.makeText(getContext(), R.string.write_username_string
                                        , Toast.LENGTH_SHORT).show();
                                break;
                            case 602:
                                Toast.makeText(getContext(), R.string.write_password_string
                                        , Toast.LENGTH_SHORT).show();
                                break;
                            case 603:
                                Toast.makeText(getContext(), R.string.incorrect_password_string
                                        , Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), R.string.error_login_string
                                        , Toast.LENGTH_SHORT).show();
                                break;

                        }

                        mainActivity.setSavedUsername("");
                        mainActivity.setSavedPassword("");

                        Navigation.findNavController(view).navigate(R.id
                                .action_splashScreenFragment_to_loginRegisterFragment);

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            });

        }

    }

}