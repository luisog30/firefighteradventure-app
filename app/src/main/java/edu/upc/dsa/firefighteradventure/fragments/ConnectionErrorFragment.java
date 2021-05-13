package edu.upc.dsa.firefighteradventure.fragments;

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
import android.widget.Button;

public class ConnectionErrorFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    private Button btnRetryConnectionError;

    public ConnectionErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_connection_error, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(false);

        btnRetryConnectionError = view.findViewById(R.id.btnRetryConnectionError);
        btnRetryConnectionError.setOnClickListener(this::btnRetryConnectionErrorClick);

    }

    public void btnRetryConnectionErrorClick(android.view.View u) {

       mainActivity.goBack();

    }

}