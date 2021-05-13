package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.Credentials.RegisterCredentials;
import edu.upc.dsa.firefighteradventure.models.UserSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class RegisterFragment extends Fragment {

    private View view;
    private EditText etUsernameRegister;
    private EditText etPasswordRegister;
    private EditText etConfirmPasswordRegister;
    private EditText etEmailRegister;
    private TextView tvBirthdateSelectedRegister;

    private Button btnBackRegister;

    private DatePickerDialog.OnDateSetListener mDataSetListener;

    private int birthdate_year;
    private int birthdate_month;
    private int birthdate_day;

    MainActivity mainActivity;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
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

        Calendar cal = Calendar.getInstance();

        setBirthdate_year(cal.get(Calendar.YEAR));
        setBirthdate_month(cal.get(Calendar.MONTH) + 1);
        setBirthdate_day(cal.get(Calendar.DAY_OF_MONTH));

        etUsernameRegister = view.findViewById(R.id.etUsernameRegister);
        etPasswordRegister = view.findViewById(R.id.etPasswordRegister);
        etConfirmPasswordRegister = view.findViewById(R.id.etConfirmPasswordRegister);
        etEmailRegister = view.findViewById(R.id.etEmailRegister);

        etUsernameRegister.setFilters(new InputFilter[] { mainActivity.spaceFilter });
        etPasswordRegister.setFilters(new InputFilter[] { mainActivity.spaceFilter });
        etConfirmPasswordRegister.setFilters(new InputFilter[] { mainActivity.spaceFilter });
        etEmailRegister.setFilters(new InputFilter[] { mainActivity.spaceFilter });

        tvBirthdateSelectedRegister = view.findViewById(R.id.tvBirthdateSelectedRegister);

        view.findViewById(R.id.btnRegister).setOnClickListener(this::btnRegisterClick);
        view.findViewById(R.id.btnBackRegister).setOnClickListener(this::btnBackRegisterClick);

        tvBirthdateSelectedRegister.setOnClickListener(this::tvBirthdateSelectedRegisterClick);

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                setBirthdate_year(year);
                setBirthdate_month(month + 1);
                setBirthdate_day(day);

                tvBirthdateSelectedRegister.setText(getBirthdate_day() + "/"
                        + getBirthdate_month() + "/" + getBirthdate_year());

            }
        };

    }

    public void tvBirthdateSelectedRegisterClick(android.view.View u) {

        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style
                .Theme_Holo_Light_Dialog_MinWidth, mDataSetListener, getBirthdate_year()
                , getBirthdate_month() - 1, getBirthdate_day());

        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void btnRegisterClick(android.view.View u){

        if (etUsernameRegister.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_username_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etPasswordRegister.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_password_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (etConfirmPasswordRegister.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_confirm_password_string, Toast
                    .LENGTH_SHORT).show();
            return;

        } else if (etEmailRegister.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_email_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (tvBirthdateSelectedRegister.getText().toString().equals((""))) {

            Toast.makeText(getContext(), R.string.write_birthdate_string, Toast.LENGTH_SHORT).show();
            return;

        } else if (!etConfirmPasswordRegister.getText().toString().equals(etPasswordRegister
                .getText().toString())) {

            Toast.makeText(getContext(), R.string.passwords_not_match_string, Toast.LENGTH_SHORT).show();
            return;

        }

        mainActivity.setLoadingData(true);

        Call<ResponseBody> resp = mainActivity.getUserService().register(
                new RegisterCredentials(etUsernameRegister.getText().toString(),
                        etPasswordRegister.getText().toString(), etEmailRegister
                        .getText().toString(), getBirthdate_year(),
                        getBirthdate_month(), getBirthdate_day()));

        resp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    Toast.makeText(getContext(), R.string.succesful_register_string,
                            Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(view)
                            .navigate(R.id.action_registerFragment_to_loginRegisterFragment);

                } else {

                    UserSettings userSettings = mainActivity.getUserSettings();

                    switch (response.code()) {

                        case 250:
                            Toast.makeText(getContext(), R.string.user_already_exists_string,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 600:
                            Toast.makeText(getContext(), R.string.write_username_string,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 601:
                            Toast.makeText(getContext(), R.string.write_password_string,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 604:
                            Toast.makeText(getContext(),
                                    getString(R.string.username_short_long_string,
                                            userSettings.getUsername_min_length(),
                                            userSettings.getUsername_max_length()),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 605:
                            Toast.makeText(getContext(),
                                    getString(R.string.password_short_long_string,
                                            userSettings.getPassword_min_length(),
                                            userSettings.getPassword_max_length()),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 606:
                            Toast.makeText(getContext(),
                                    getString(R.string.email_short_long_string,
                                            userSettings.getEmail_min_length(),
                                            userSettings.getEmail_max_length()),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 607:
                            Toast.makeText(getContext(), getString(R.string.too_young_string,
                                    userSettings.getMin_age()),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 608:
                            Toast.makeText(getContext(), R.string.email_already_exists_string,
                                    Toast.LENGTH_SHORT).show();
                        default:
                            Toast.makeText(getContext(), R.string.error_register_string,
                                    Toast.LENGTH_SHORT).show();
                            break;

                    }

                 }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }
        });

    }

    public void btnBackRegisterClick(android.view.View u) {

        mainActivity.goBack();

    }

    public int getBirthdate_year() {
        return birthdate_year;
    }

    public void setBirthdate_year(int birthdate_year) {
        this.birthdate_year = birthdate_year;
    }

    public int getBirthdate_month() {
        return birthdate_month;
    }

    public void setBirthdate_month(int birthdate_month) {
        this.birthdate_month = birthdate_month;
    }

    public int getBirthdate_day() {
        return birthdate_day;
    }

    public void setBirthdate_day(int birthdate_day) {
        this.birthdate_day = birthdate_day;
    }
}