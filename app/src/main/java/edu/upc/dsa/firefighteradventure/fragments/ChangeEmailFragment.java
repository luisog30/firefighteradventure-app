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

public class ChangeEmailFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackChangeEmail;
    private Button btnChangeEmail;

    private EditText etPasswordChangeEmail;
    private EditText etNewEmailChangeEmail;
    private EditText etConfirmNewEmailChangeEmail;

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_email, container, false);
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

        btnBackChangeEmail = view.findViewById(R.id.btnBackChangeEmail);
        btnChangeEmail = view.findViewById(R.id.btnChangeEmail);
        etPasswordChangeEmail = view.findViewById(R.id.etPasswordChangeEmail);
        etNewEmailChangeEmail = view.findViewById(R.id.etNewEmailChangeEmail);
        etConfirmNewEmailChangeEmail = view.findViewById(R.id.etConfirmNewEmailChangeEmail);

        btnBackChangeEmail.setOnClickListener(this::btnBackChangeEmailClick);
        btnChangeEmail.setOnClickListener(this::btnChangeEmailClick);

    }

    public void btnChangeEmailClick(android.view.View u) {

        if (etPasswordChangeEmail.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_current_password_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etNewEmailChangeEmail.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_new_email_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etConfirmNewEmailChangeEmail.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_confirm_new_email, Toast.LENGTH_SHORT).show();
            return;

        } else if (!etNewEmailChangeEmail.getText().toString().equals(etConfirmNewEmailChangeEmail.getText().toString())) {

            Toast.makeText(getContext(), R.string.emails_do_not_match_string, Toast.LENGTH_SHORT).show();
            return;

        }

        mainActivity.setLoadingData(true);

        Call<ResponseBody> resp = mainActivity.getUserService()
                .updateParameterByUsername(mainActivity.getSavedUsername(), "email",
                        new UpdateParameterRequest(etNewEmailChangeEmail.getText().toString()));

        resp.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:
                        Toast.makeText(getContext(), R.string.email_changed_string, Toast.LENGTH_SHORT).show();

                        Navigation.findNavController(view).navigate(R.id.mainMenuFragment);
                        break;

                    case 602:
                        Toast.makeText(getContext(), R.string.write_current_password_string, Toast.LENGTH_SHORT).show();
                        break;

                    case 605:
                        Toast.makeText(getContext(), R.string.incorrect_password_string, Toast.LENGTH_SHORT).show();
                        break;

                    case 604:
                        Toast.makeText(getContext(), R.string.write_new_email_string, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public void btnBackChangeEmailClick(android.view.View u) {

        mainActivity.goBack();

    }

}