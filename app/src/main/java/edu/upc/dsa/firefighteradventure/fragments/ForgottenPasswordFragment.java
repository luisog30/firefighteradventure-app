package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.Credentials.LoginCredentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class ForgottenPasswordFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackForgottenPassword;
    private Button btnRecoverPassword;

    private EditText etUsernameForgottenPassword;

    public ForgottenPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgotten_password, container, false);
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

        btnBackForgottenPassword = view.findViewById(R.id.btnBackForgottenPassword);
        btnRecoverPassword = view.findViewById(R.id.btnRecoverPassword);
        etUsernameForgottenPassword = view.findViewById(R.id.etUsernameForgottenPassword);

        btnBackForgottenPassword.setOnClickListener(this::btnBackForgottenPasswordClick);
        btnRecoverPassword.setOnClickListener(this::btnRecoverPasswordClick);

    }

    public void btnBackForgottenPasswordClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void btnRecoverPasswordClick(android.view.View u) {

        if (etUsernameForgottenPassword.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_username_string, Toast.LENGTH_SHORT).show();

        }

        mainActivity.setLoadingData(true);

        Call<ResponseBody> resp = mainActivity.getUserService().forgottenPassword(
                etUsernameForgottenPassword.getText().toString());

        resp.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    Toast.makeText(getContext(), R.string.we_have_sent_you_an_email_string,
                            Toast.LENGTH_SHORT).show();

                } else if (response.code() == 404) {

                    Toast.makeText(getContext(), R.string.user_not_exists_string,
                            Toast.LENGTH_SHORT).show();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

}