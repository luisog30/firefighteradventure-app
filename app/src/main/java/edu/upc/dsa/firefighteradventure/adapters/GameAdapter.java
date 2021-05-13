package edu.upc.dsa.firefighteradventure.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.fragments.GameFragment;
import edu.upc.dsa.firefighteradventure.models.Game;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> values;
    private MainActivity mainActivity;
    private GameFragment gameFragment;

    private String destination;

    private int positionClicked;


    private View gview;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLastDateTimeGame;
        public TextView tvCreatedDateTimeGame;
        public TextView tvMapGame;
        public TextView tvScoreGame;
        public TextView tvCoinsGame;
        public TextView tvHealthGame;
        public TextView tvAttackGame;
        public TextView tvDefenseGame;
        public TextView tvHealingGame;
        public TextView tvSpeedGame;

        public ProgressBar pbHealthGame;
        public ProgressBar pbAttackGame;
        public ProgressBar pbDefenseGame;
        public ProgressBar pbHealingGame;
        public ProgressBar pbSpeedGame;

        public Button btnDeleteGame;

        public View view;

        public ViewHolder(View view) {

            super(view);
            this.view = view;
            gview = view;
            tvLastDateTimeGame = view.findViewById(R.id.tvNameOrder);
            tvCreatedDateTimeGame = view.findViewById(R.id.tvCreatedDateTimeGame);
            tvMapGame = view.findViewById(R.id.tvDescriptionOrder);
            tvScoreGame = view.findViewById(R.id.tvBoughtAtOn);
            tvCoinsGame = view.findViewById(R.id.tvPriceOrder);
            tvHealthGame = view.findViewById(R.id.tvHealthGame);
            tvAttackGame = view.findViewById(R.id.tvAttackGame);
            tvDefenseGame = view.findViewById(R.id.tvDefenseGame);
            tvHealingGame = view.findViewById(R.id.tvHealingGame);
            tvSpeedGame = view.findViewById(R.id.tvSpeedGame);

            pbHealthGame = view.findViewById(R.id.pbHealthGame);
            pbAttackGame = view.findViewById(R.id.pbAttackGame);
            pbDefenseGame = view.findViewById(R.id.pbDefenseGame);
            pbHealingGame = view.findViewById(R.id.pbHealingGame);
            pbSpeedGame = view.findViewById(R.id.pbSpeedGame);

            btnDeleteGame = view.findViewById(R.id.btnDeleteGame);

        }

    }

    public void add(int position, Game game) {
        values.add(position, game);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);

        if (values.size() == 0) {
            gameFragment.getTvNoGames().setVisibility(View.VISIBLE);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GameAdapter(List<Game> myDataset, GameFragment gameFragment, String destination) {

        values = myDataset;
        this.gameFragment = gameFragment;
        this.destination = destination;

        this.mainActivity = gameFragment.getMainActivity();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.game_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){

                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    mainActivity.setLoadingData(true);

                    Call<ResponseBody> resp = mainActivity.getGameService()
                            .deleteGameById(values.get(positionClicked).getId());

                    resp.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call
                                , Response<ResponseBody> response) {

                            mainActivity.setLoadingData(false);

                            if (response.code() == 200) {

                                Toast.makeText(mainActivity, R.string.game_deleted_string
                                        , Toast.LENGTH_SHORT).show();

                                remove(positionClicked);

                            } else if (response.code() == 404) {

                                Toast.makeText(mainActivity, R.string.game_does_not_exist_string
                                        , Toast.LENGTH_SHORT).show();

                            } else {

                                Navigation.findNavController(gview)
                                        .navigate(R.id.connectionErrorFragment);

                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            mainActivity.setLoadingData(false);
                            Navigation.findNavController(gview).navigate(R.id.connectionErrorFragment);

                        }

                    });
                    break;
            }

        }

    };


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Game game = values.get(position);

        holder.btnDeleteGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                positionClicked = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setMessage(R.string.are_you_sure_delete_game_string).
                        setPositiveButton(R.string.no_string, dialogClickListener).
                        setNegativeButton(R.string.yes_string
                        , dialogClickListener).setTitle(R.string.delete_game_string).show();

            }
        });


        if (game.getTimeEnd().equals("") && game.getDateEnd().equals("")) {

            holder.view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();

                    switch (destination) {

                        case "play":

                            bundle.putInt("id_game", game.getId());
                            Navigation.findNavController(view).navigate(R.id
                                    .action_gameFragment_to_playFragment, bundle);
                            break;

                        case "shop_upgrades":
                            bundle.putInt("id_game", game.getId());
                            bundle.putInt("coins", game.getCoins());
                            Navigation.findNavController(view).navigate(R.id
                                    .action_gameFragment_to_shopUpgradesFragment, bundle);

                            break;

                    }

                }
            });

            holder.tvLastDateTimeGame.setText(mainActivity
                    .getString(R.string.last_access_at_on_string
                            , game.getTimeLast(), game.getDateLast()));

            holder.view.setBackgroundTintList(mainActivity.getResources()
                    .getColorStateList(R.color.colorPrimary));

        } else {

            holder.tvLastDateTimeGame.setText(mainActivity
                    .getString(R.string.finished_at_on_string
                            , game.getTimeEnd(), game.getDateEnd()));


            holder.view.setBackgroundTintList(mainActivity.getResources()
                    .getColorStateList(android.R.color.darker_gray));


        }

        holder.tvCreatedDateTimeGame.setText(mainActivity
                .getString(R.string.created_at_on_string,
                        game.getTimeStart(), game.getDateStart()));

        holder.tvMapGame.setText(mainActivity.getString(R.string.map_string)
                + ": " + mainActivity.getMapById(game.getId_map()).getName());

        holder.tvScoreGame.setText(mainActivity.getString(R.string.score_string) + ": "
                + game.getScore());

        holder.tvCoinsGame.setText(mainActivity.getString(R.string.coins_string) + ": "
                + game.getCoins());

        holder.tvHealthGame.setText(mainActivity.getString(R.string.health_string) + ": "
                + game.getHealth() + "/" + mainActivity.getMaxHealth(game.getDefense()));

        holder.pbHealthGame.setProgress(100 * game.getHealth()
                / mainActivity.getMaxHealth(game.getDefense()));

        holder.tvAttackGame.setText(mainActivity.getString(R.string.attack_string) + ": "
                + game.getAttack() + "/" + mainActivity.getGameSettings().getMax_attack());

        holder.pbAttackGame.setProgress(100 * game.getAttack()
                / mainActivity.getGameSettings().getMax_attack());

        holder.tvHealingGame.setText(mainActivity.getString(R.string.healing_string) + ": "
                + game.getHealing() + "/" + mainActivity.getGameSettings().getMax_healing());

        holder.pbHealingGame.setProgress(100 * game.getHealing()
                / mainActivity.getGameSettings().getMax_healing());

        holder.tvDefenseGame.setText(mainActivity.getString(R.string.defense_string) + ": "
                + game.getDefense() + "/" + mainActivity.getGameSettings().getMax_defense());

        holder.pbDefenseGame.setProgress(100 * game.getDefense()
                / mainActivity.getGameSettings().getMax_defense());

        holder.tvSpeedGame.setText(mainActivity.getString(R.string.speed_string) + ": "
                + game.getSpeed() + "/" + mainActivity.getGameSettings().getMax_speed());

        holder.pbSpeedGame.setProgress(100 * game.getSpeed()
                / mainActivity.getGameSettings().getMax_speed());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
