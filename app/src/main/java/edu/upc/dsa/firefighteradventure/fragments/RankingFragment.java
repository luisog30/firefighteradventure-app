package edu.upc.dsa.firefighteradventure.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.adapters.RankingAdapter;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.UserRanking;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RankingFragment extends Fragment {

    private View view;

    private MainActivity mainActivity;

    private Button btnBackRanking;
    private Button btnSearchUsernameRanking;

    private RecyclerView rvRanking;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView tvYourPositionIs;
    private EditText etSearchUsernameRanking;

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ranking, container, false);
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

        btnBackRanking = view.findViewById(R.id.btnBackRanking);
        btnSearchUsernameRanking = view.findViewById(R.id.btnSearchUsernameRanking);
        tvYourPositionIs = view.findViewById(R.id.tvYourPositionIs);
        etSearchUsernameRanking = view.findViewById(R.id.etSearchUsernameRanking);

        rvRanking = view.findViewById(R.id.rvRanking);

        etSearchUsernameRanking.setFilters(new InputFilter[] { mainActivity.spaceFilter });
        btnBackRanking.setOnClickListener(this::btnBackRankingClick);
        btnSearchUsernameRanking.setOnClickListener(this::btnSearchUsernameRankingClick);

        mainActivity.setLoadingData(true);

        Call<List<UserRanking>> users = mainActivity.getUserService().getUserRanking();

        users.enqueue(new Callback<List<UserRanking>>() {

            @Override
            public void onResponse(Call<List<UserRanking>> call, Response<List<UserRanking>> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    List<UserRanking> result = response.body();

                    rvRanking.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getContext());
                    rvRanking.setLayoutManager(layoutManager);

                    mAdapter = new RankingAdapter(result, mainActivity);
                    rvRanking.setAdapter(mAdapter);

                    getRankingPosition();

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<UserRanking>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public void getRankingPosition() {

        mainActivity.setLoadingData(true);

        Call<UserRanking> rankingPos = mainActivity.getUserService().getUserRankingByUsername(mainActivity.getSavedUsername());

        rankingPos.enqueue(new Callback<UserRanking>() {

            @Override
            public void onResponse(Call<UserRanking> call, Response<UserRanking> response) {

                mainActivity.setLoadingData(false);

                switch(response.code()) {

                    case 200:
                        UserRanking result = response.body();
                        tvYourPositionIs.setText(tvYourPositionIs.getText().toString() + " " + result.getPosition());
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
            public void onFailure(Call<UserRanking> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public void btnSearchUsernameRankingClick(android.view.View u) {

        if (etSearchUsernameRanking.getText().toString().equals("")) {

            Toast.makeText(getContext(), R.string.write_username_string, Toast.LENGTH_SHORT).show();
            return;

        }

        mainActivity.setLoadingData(true);

        Call<UserProfile> userProfile = mainActivity.getUserService().getUserProfileByUsername(etSearchUsernameRanking.getText().toString());

        userProfile.enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:

                        UserProfile userProfile = response.body();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userProfile", userProfile);

                        Navigation.findNavController(view).navigate(R.id.profileGeneralFragment, bundle);

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

    public void btnBackRankingClick(android.view.View u) {

        mainActivity.goBack();

    }

}