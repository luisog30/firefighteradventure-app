package edu.upc.dsa.firefighteradventure.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteAccountFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnDeleteAccount;
    private CheckBox cbDeleteAccount;
    private EditText etPasswordDeleteAccount;
    private EditText etConfirmPasswordDeleteAccount;

    private Button btnBackDeleteAccount;

    public DeleteAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delete_account, container, false);
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

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        cbDeleteAccount = view.findViewById(R.id.cbDeleteAccount);
        etPasswordDeleteAccount = view.findViewById(R.id.etPasswordDeleteAccount);
        etConfirmPasswordDeleteAccount = view.findViewById(R.id.etConfirmPasswordDeleteAccount);
        btnBackDeleteAccount = view.findViewById(R.id.btnBackDeleteAccount);

        btnDeleteAccount.setOnClickListener(this::btnDeleteAccountClick);
        btnBackDeleteAccount.setOnClickListener(this::btnBackDeleteAccountClick);

    }

    public void btnDeleteAccountClick(android.view.View u) {

        if (!cbDeleteAccount.isChecked()) {

            Toast.makeText(getContext(), R.string.must_confirm_account_deletion_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etPasswordDeleteAccount.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_password_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etConfirmPasswordDeleteAccount.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_confirm_password_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (!etPasswordDeleteAccount.getText().toString().equals(etConfirmPasswordDeleteAccount.getText().toString())) {

            Toast.makeText(getContext(), R.string.passwords_not_match_string, Toast.LENGTH_SHORT).show();
            return;

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.you_sure_delete_account_string).setPositiveButton(R.string.no_string, dialogClickListener)
                .setNegativeButton(R.string.yes_string, dialogClickListener).setTitle(R.string.delete_account_two_string).show();


    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){

                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    mainActivity.setLoadingData(true);

                    Call<ResponseBody> resp = mainActivity.getUserService().deleteUserByUsername(mainActivity.getSavedUsername());

                    resp.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            mainActivity.setLoadingData(false);

                            switch (response.code()) {

                                case 200:
                                    Toast.makeText(getContext(), R.string.account_deleted_string,
                                            Toast.LENGTH_SHORT).show();

                                    mainActivity.setSavedUsername("");
                                    mainActivity.setSavedPassword("");

                                    Navigation.findNavController(view).navigate(R.id.loginRegisterFragment);
                                    break;

                                case 404:
                                    Toast.makeText(getContext(), R.string.user_not_exists_string,
                                            Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    Navigation.findNavController(view)
                                            .navigate(R.id.loginRegisterFragment);
                                    break;

                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            mainActivity.setLoadingData(false);
                            Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                        }

                    });

                    break;

            }

        }

    };

    public void btnBackDeleteAccountClick(android.view.View u) {

        mainActivity.goBack();

    }

}