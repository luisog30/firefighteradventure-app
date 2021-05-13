package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.UpdateParameterRequest;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private View view;
    private Button btnGotoChangePassword;
    private Button btnGotoChangeEmail;
    private Button btnGotoDeleteAccount;

    private Button btnBackProfile;
    private Button btnChangeProfilePhoto;

    private TextView tvProfileUsername;
    private TextView tvProfileBirthdate;
    private TextView tvProfileEmail;
    private TextView tvProfileScore;
    private TextView tvProfileRankingPosition;
    private ImageView ivProfilePhoto;

    private MainActivity mainActivity;

    private String profile_photo_url;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
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

        btnGotoChangePassword = view.findViewById(R.id.btnGotoChangePassword);
        btnGotoChangeEmail = view.findViewById(R.id.btnGotoChangeEmail);
        btnGotoDeleteAccount = view.findViewById(R.id.btnGotoDeleteAccount);
        btnBackProfile = view.findViewById(R.id.btnBackProfile);
        btnChangeProfilePhoto = view.findViewById(R.id.btnChangeProfilePhoto);

        btnChangeProfilePhoto.setOnClickListener(this::btnChangeProfilePhotoClick);
        btnGotoChangePassword.setOnClickListener(this::btnGotoChangePasswordClick);
        btnGotoChangeEmail.setOnClickListener(this::btnGotoChangeEmailClick);
        btnGotoDeleteAccount.setOnClickListener(this::btnGotoDeleteAccountClick);
        btnBackProfile.setOnClickListener(this::btnBackProfileClick);

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileBirthdate = view.findViewById(R.id.tvProfileBirthdate);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfileScore = view.findViewById(R.id.tvProfileScore);
        tvProfileRankingPosition = view.findViewById(R.id.tvProfileRankingPosition);

        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);


        UserProfile userProfile = (UserProfile) getArguments().getSerializable("userProfile");

        tvProfileUsername.setText(userProfile.getUsername());
        tvProfileBirthdate.setText(getString(R.string.birthdate_string) + ": " + userProfile.getBirthdate());
        tvProfileEmail.setText(userProfile.getEmail());
        tvProfileScore.setText(getString(R.string.score_string) + ": " + userProfile.getScore());
        tvProfileRankingPosition.setText(getString(R.string.ranking_position_string) + ": " + userProfile.getRanking_position());

        this.profile_photo_url = userProfile.getProfile_photo();

        Picasso.get().load(getProfile_photo_url()).resize(130, 130)
                .centerCrop(Gravity.CENTER)
                .into(ivProfilePhoto);

        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("image", getProfile_photo_url());
                Navigation.findNavController(view).navigate(R.id.imageViewFragment, bundle);
            }
        });

    }

    public void btnGotoChangePasswordClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_changePasswordFragment);

    }

    public void btnGotoChangeEmailClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_changeEmailFragment);

    }

    public void btnGotoDeleteAccountClick(android.view.View u) {

        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_deleteAccountFragment);

    }

    public void btnBackProfileClick(android.view.View u) {

        mainActivity.goBack();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void btnChangeProfilePhotoClick(android.view.View u) {

        UserProfile userProfile = (UserProfile) getArguments().getSerializable("userProfile");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(R.string.profile_photo_url_string);
        alertDialog.setMessage(R.string.enter_new_profile_photo_url_string);

        EditText input = new EditText(getContext());

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50,0);

        input.setText(this.profile_photo_url);

        input.setBackground(getActivity().getDrawable(R.drawable.textbox));
        input.setLayoutParams(lp);
        input.setTextSize(14);
        input.setGravity(Gravity.CENTER);
        input.setBackgroundTintList(getActivity().getColorStateList(R.color.colorPrimary));

        container.addView(input);
        alertDialog.setView(container);


        alertDialog.setPositiveButton(R.string.change_string,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().equals("")) {

                            Toast.makeText(getContext(), R.string.must_enter_a_url_string,
                                    Toast.LENGTH_SHORT).show();
                            return;

                        }

                        mainActivity.setLoadingData(true);

                        Call<ResponseBody> resp = mainActivity.getUserService()
                                .updateParameterByUsername(mainActivity.getSavedUsername()
                                , "profile_photo", new UpdateParameterRequest(
                                        input.getText().toString()));

                        resp.enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call,
                                                   Response<ResponseBody> response) {

                                mainActivity.setLoadingData(false);

                                switch (response.code()) {

                                    case 200:
                                        Toast.makeText(getContext(), R.string
                                                        .you_have_changed_your_profile_photo_string,
                                                Toast.LENGTH_SHORT).show();

                                        setProfile_photo_url(input.getText().toString());

                                        Picasso.get().load(getProfile_photo_url()).resize(
                                                130, 130)
                                                .centerCrop(Gravity.CENTER)
                                                .into(ivProfilePhoto);

                                        break;

                                    case 404:
                                        Toast.makeText(getContext(), R.string
                                                        .user_not_exists_string,
                                                Toast.LENGTH_SHORT).show();
                                        break;

                                    case 604:
                                        Toast.makeText(getContext(), R.string
                                                        .must_enter_a_url_string,
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

                    }
                });


        alertDialog.setNegativeButton(R.string.back_string,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }
}