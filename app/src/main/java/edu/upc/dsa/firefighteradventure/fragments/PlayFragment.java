package edu.upc.dsa.firefighteradventure.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unity3d.player.UnityPlayerActivity;

public class PlayFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private int id_game;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play, container, false);
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

        this.id_game = getArguments().getInt("id_game");

        Intent intent = new Intent(this.mainActivity, UnityPlayerActivity.class);
        startActivity(intent);

    }

}