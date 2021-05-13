package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginRegisterFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public LoginRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login_register, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(false);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        view.findViewById(R.id.btnGotoLogin).setOnClickListener(this::btnGotoLoginClick);
        view.findViewById(R.id.btnGotoRegister).setOnClickListener(this::btnGotoRegisterClick);
        view.findViewById(R.id.btnGotoDevelopers).setOnClickListener(this::btnGotoDevelopersClick);
        view.findViewById(R.id.btnGotoConfigurationLoginRegister).setOnClickListener(this::btnGotoConfigurationLoginRegisterClick);

    }

    public void btnGotoRegisterClick(android.view.View u){

        Navigation.findNavController(view).navigate(R.id.action_loginRegisterFragment_to_registerFragment);

    }

    public void btnGotoLoginClick(android.view.View u){

        Navigation.findNavController(view).navigate(R.id.action_loginRegisterFragment_to_loginFragment);

    }

    public void btnGotoDevelopersClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_loginRegisterFragment_to_developersFragment);

    }

    public void btnGotoConfigurationLoginRegisterClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.configurationFragment);

    }

}