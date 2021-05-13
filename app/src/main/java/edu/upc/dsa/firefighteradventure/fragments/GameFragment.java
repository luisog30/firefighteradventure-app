package edu.upc.dsa.firefighteradventure.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.adapters.GameAdapter;
import edu.upc.dsa.firefighteradventure.models.Game;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class GameFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    private Button btnBackGame;
    private Button btnNewGame;

    private TextView tvNoGames;

    private String destination;

    private RecyclerView rvGames;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GameFragment gameFragment;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game, container, false);
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

        btnBackGame = view.findViewById(R.id.btnBackGame);
        btnNewGame = view.findViewById(R.id.btnNewGame);
        rvGames = view.findViewById(R.id.rvOrders);
        tvNoGames = view.findViewById(R.id.tvNoGames);

        btnBackGame.setOnClickListener(this::btnBackGameClick);
        btnNewGame.setOnClickListener(this::btnNewGameClick);

        destination = getArguments().getString("destination");

        this.gameFragment = this;

        loadGames();

    }

    public void btnBackGameClick(android.view.View u) {

        mainActivity.goBack();

    }

    public void btnNewGameClick(android.view.View u) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(R.string.want_to_create_new_game_string).
                setPositiveButton(R.string.no_string, dialogClickListener).
                setNegativeButton(R.string.yes_string
                        , dialogClickListener).setTitle(R.string.create_new_game_string).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){

                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    mainActivity.setLoadingData(true);

                    Call<ResponseBody> resp = mainActivity.getUserService()
                            .createGameByUsername(mainActivity.getSavedUsername());

                    resp.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call
                                , Response<ResponseBody> response) {

                            if (response.code() == 201) {

                                Toast.makeText(mainActivity, R.string.game_created_string
                                        , Toast.LENGTH_SHORT).show();

                                loadGames();

                            } else {

                                Navigation.findNavController(view)
                                        .navigate(R.id.connectionErrorFragment);

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


    public void loadGames() {

        mainActivity.setLoadingData(true);

        Call<List<Game>> games = mainActivity.getUserService()
                .getUserGamesByUsername(mainActivity.getSavedUsername());

        games.enqueue(new Callback<List<Game>>() {

            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {

                mainActivity.setLoadingData(false);

                if (response.code() == 200) {

                    List<Game> result = response.body();

                    tvNoGames.setVisibility(View.VISIBLE);

                    if (result.size() != 0) {

                        tvNoGames.setVisibility(View.GONE);

                    }

                    Collections.sort(result, new Comparator<Game>() {
                        public int compare(Game o1, Game o2) {

                            String d1;
                            String d2;

                            String t1;
                            String t2;

                            if (o1.getTimeEnd().equals("") && o1.getDateEnd().equals("")) {

                                d1 = o1.getDateLast();
                                t1 = o1.getTimeLast();

                            } else {

                                d1 = o1.getDateEnd();
                                t1 = o1.getTimeEnd();

                            }

                            if (o2.getTimeEnd().equals("") && o2.getDateEnd().equals("")) {

                                d2 = o2.getDateLast();
                                t2 = o2.getTimeLast();

                            } else {

                                d2 = o2.getDateEnd();
                                t2 = o2.getTimeEnd();

                            }

                            Date date1 = Calendar.getInstance().getTime();
                            Date date2 = Calendar.getInstance().getTime();
                            Date time1 = Calendar.getInstance().getTime();
                            Date time2 = Calendar.getInstance().getTime();

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format =
                                    new SimpleDateFormat("dd/MM/yyyy");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 =
                                    new SimpleDateFormat("HH:mm");
                            try {
                                date1 = format.parse(d1);
                                date2 = format.parse(d2);

                                time1 = format2.parse(t1);
                                time2 = format2.parse(t2);
                            } catch (ParseException e) {

                            }

                            if (date1.compareTo(date2) == 0) {

                                return time2.compareTo(time1);

                            } else {

                                return date2.compareTo(date1) * 1000000;

                            }

                        }
                    });

                    rvGames.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getContext());
                    rvGames.setLayoutManager(layoutManager);

                    mAdapter = new GameAdapter(result, gameFragment, destination);
                    rvGames.setAdapter(mAdapter);

                } else {

                    Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

                }

            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }

    public TextView getTvNoGames() {
        return tvNoGames;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

}