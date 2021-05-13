package edu.upc.dsa.firefighteradventure.fragments;

import android.app.AlertDialog;
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
import android.widget.ImageView;


public class DevelopersFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    private ImageView ivSumaya;
    private ImageView ivEric;
    private ImageView ivMeritxell;
    private ImageView ivSuhail;
    private ImageView ivLluis;
    private ImageView ivGerard;

    private Button btnBackDevelopers;

    public DevelopersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_developers, container, false);
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

        ivSumaya = view.findViewById(R.id.ivSumaya);
        ivEric = view.findViewById(R.id.ivEric);
        ivMeritxell = view.findViewById(R.id.ivMeritxell);
        ivSuhail = view.findViewById(R.id.ivSuhail);
        ivLluis = view.findViewById(R.id.ivLluis);
        ivGerard = view.findViewById(R.id.ivGerard);

        btnBackDevelopers = view.findViewById(R.id.btnBackDevelopers);

        ivSumaya.setOnClickListener(this::ivSumayaClick);
        ivEric.setOnClickListener(this::ivEricClick);
        ivMeritxell.setOnClickListener(this::ivMeritxellClick);
        ivSuhail.setOnClickListener(this::ivSuhailClick);
        ivLluis.setOnClickListener(this::ivLluisClick);
        ivGerard.setOnClickListener(this::ivGerardClick);

        btnBackDevelopers.setOnClickListener(this::btnBackDevelopersClick);



    }

    public void ivSumayaClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Web developer").setTitle(R.string.sumaya_string).show();

    }

    public void ivEricClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("BackEnd developer").setTitle(R.string.eric_string).show();

    }

    public void ivMeritxellClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("BackEnd developer").setTitle(R.string.meritxell_string).show();

    }

    public void ivSuhailClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Unity Developer").setTitle(R.string.suhail_string).show();

    }

    public void ivLluisClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Android developer").setTitle(R.string.lluis_string).show();

    }

    public void ivGerardClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Android developer").setTitle(R.string.gerard_string).show();

    }

    public void btnBackDevelopersClick(android.view.View u) {

        mainActivity.goBack();

    }

}