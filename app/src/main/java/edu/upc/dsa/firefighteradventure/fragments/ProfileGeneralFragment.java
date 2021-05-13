package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.User;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProfileGeneralFragment extends Fragment {

    private View view;
    MainActivity mainActivity;

    private Button btnBackProfileGeneral;

    private String username;

    private TextView tvProfileGeneralUsername;
    private TextView tvProfileGeneralBirthdate;
    private TextView tvProfileGeneralEmail;
    private TextView tvProfileGeneralScore;

    private TextView tvProfileGeneralRankingPosition;

    private ImageView ivProfileGeneralPhoto;

    private String profile_photo_url;

    public ProfileGeneralFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getArguments().getString("username");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_general, container, false);
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

        tvProfileGeneralUsername = view.findViewById(R.id.tvProfileGeneralUsername);
        tvProfileGeneralBirthdate = view.findViewById(R.id.tvProfileGeneralBirthdate);
        tvProfileGeneralEmail = view.findViewById(R.id.tvProfileGeneralEmail);
        tvProfileGeneralScore = view.findViewById(R.id.tvProfileGeneralScore);

        btnBackProfileGeneral = view.findViewById(R.id.btnBackProfileGeneral);

        tvProfileGeneralRankingPosition = view.findViewById(R.id.tvProfileGeneralRankingPosition);

        btnBackProfileGeneral.setOnClickListener(this::btnBackProfileGeneralClick);

        ivProfileGeneralPhoto = view.findViewById(R.id.ivProfileGeneralPhoto);




        UserProfile userProfile = (UserProfile) getArguments().getSerializable("userProfile");

        tvProfileGeneralUsername.setText(userProfile.getUsername());
        tvProfileGeneralBirthdate.setText(getString(R.string.birthdate_string) + ": " + userProfile.getBirthdate());
        tvProfileGeneralEmail.setText(userProfile.getEmail());
        tvProfileGeneralScore.setText(getString(R.string.score_string) + ": " + userProfile.getScore());
        tvProfileGeneralRankingPosition.setText(getString(R.string.ranking_position_string) + ": " + userProfile.getRanking_position());

        this.profile_photo_url = userProfile.getProfile_photo();

        Picasso.get().load(getProfile_photo_url()).resize(130, 130)
                .centerCrop(Gravity.CENTER)
                .into(ivProfileGeneralPhoto);


        ivProfileGeneralPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("image", getProfile_photo_url());
                Navigation.findNavController(view).navigate(R.id.imageViewFragment, bundle);
            }
        });


    }

    public void btnBackProfileGeneralClick(android.view.View u) {

        mainActivity.goBack();

    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }
}