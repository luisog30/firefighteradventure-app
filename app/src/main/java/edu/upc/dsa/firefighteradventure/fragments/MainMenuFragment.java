package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainMenuFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnGotoPlay).setOnClickListener(this::btnGotoPlayClick);
        view.findViewById(R.id.btnGotoProfile).setOnClickListener(this::btnGotoProfileClick);
        view.findViewById(R.id.btnGotoShopUpgrades).setOnClickListener(this::btnGotoShopUpgradesClick);
        view.findViewById(R.id.btnLogout).setOnClickListener(this::btnLogoutClick);
        view.findViewById(R.id.btnGotoRanking).setOnClickListener(this::btnGotoRankingClick);
        view.findViewById(R.id.btnGotoConfigurationMainMenu).setOnClickListener(this::btnGotoConfigurationMainMenuClick);
        view.findViewById(R.id.btnGotoOrders).setOnClickListener(this::btnGotoOrdersClick);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(false);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        TextView tvHello = view.findViewById(R.id.tvHello);
        tvHello.setText(getString(R.string.hello_string) + " " + mainActivity.getSavedUsername() + "!");

    }

    public void btnGotoPlayClick(android.view.View u) {

        Bundle bundle = new Bundle();
        bundle.putString("destination", "play");

        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_gameFragment
                , bundle);

    }

    public void btnGotoProfileClick(android.view.View u) {

        mainActivity.setLoadingData(true);

        Call<UserProfile> userProfile = mainActivity.getUserService().getUserProfileByUsername(mainActivity.getSavedUsername());

        userProfile.enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:
                        UserProfile userProfile = response.body();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userProfile", userProfile);

                        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_profileFragment, bundle);

                        break;

                    case 404:
                        Toast.makeText(getContext(), R.string.user_not_exists_string, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public void btnGotoShopUpgradesClick(android.view.View u) {

        Bundle bundle = new Bundle();
        bundle.putString("destination", "shop_upgrades");

        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_gameFragment
                , bundle);

    }

    public void btnGotoOrdersClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_ordersFragment);

    }

    public void btnGotoRankingClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_rankingFragment);

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){

                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    Snackbar.make(view, R.string.logged_out_string, Snackbar.LENGTH_SHORT)
                            .show();

                    mainActivity.setSavedUsername("");
                    mainActivity.setSavedPassword("");

                    Navigation.findNavController(view).navigate(R.id.loginRegisterFragment);

                    break;

            }

        }

    };

    public void btnLogoutClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.confirm_logout_string).setPositiveButton(R.string.no_string, dialogClickListener)
                .setNegativeButton(R.string.yes_string, dialogClickListener).setTitle(R.string.logout_string).show();

    }

    public void btnGotoConfigurationMainMenuClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.configurationFragment);

    }

}