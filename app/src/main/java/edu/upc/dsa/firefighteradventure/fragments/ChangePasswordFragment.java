package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.UpdateParameterRequest;
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

public class ChangePasswordFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackChangePassword;
    private Button btnChangePassword;

    private EditText etPasswordChangePassword;
    private EditText etNewPasswordChangePassword;
    private EditText etConfirmNewPasswordChangePassword;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
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

        btnBackChangePassword = view.findViewById(R.id.btnBackChangePassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        etPasswordChangePassword = view.findViewById(R.id.etPasswordChangePassword);
        etNewPasswordChangePassword = view.findViewById(R.id.etNewPasswordChangePassword);
        etConfirmNewPasswordChangePassword = view.findViewById(R.id.etConfirmNewPasswordChangePassword);

        btnChangePassword.setOnClickListener(this::btnChangePasswordClick);
        btnBackChangePassword.setOnClickListener(this::btnBackChangePasswordClick);

    }

    public void btnBackChangePasswordClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void btnChangePasswordClick(android.view.View u) {

        if (etPasswordChangePassword.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_current_password_string,
                    Toast.LENGTH_SHORT).show();

            return;

        } else if (etNewPasswordChangePassword.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_new_password_string,
                    Toast.LENGTH_SHORT).show();

            return;

        } else if (etConfirmNewPasswordChangePassword.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_confirm_new_password,
                    Toast.LENGTH_SHORT).show();

            return;

        } else if (!etNewPasswordChangePassword.getText().toString()
                .equals(etConfirmNewPasswordChangePassword.getText().toString())) {

            Toast.makeText(getContext(), R.string.passwords_not_match_string, Toast.LENGTH_SHORT).show();
            return;

        }

        mainActivity.setLoadingData(true);

        Call<ResponseBody> resp = mainActivity.getUserService()
                .updateParameterByUsername(mainActivity.getSavedUsername(),
                        "password",
                        new UpdateParameterRequest(etNewPasswordChangePassword.getText()
                                .toString()));

        resp.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:
                        Toast.makeText(getContext(), R.string.password_changed_string,
                                Toast.LENGTH_SHORT).show();

                        mainActivity.setSavedPassword(etNewPasswordChangePassword.getText().toString());

                        Navigation.findNavController(view).navigate(R.id.mainMenuFragment);

                        break;

                    case 404:
                        Toast.makeText(getContext(), R.string.user_not_exists_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    case 602:
                        Toast.makeText(getContext(), R.string.write_current_password_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    case 605:
                        Toast.makeText(getContext(), R.string.incorrect_password_string,
                                Toast.LENGTH_SHORT).show();
                        break;

                    case 604:
                        Toast.makeText(getContext(), R.string.write_new_password_string,
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

}