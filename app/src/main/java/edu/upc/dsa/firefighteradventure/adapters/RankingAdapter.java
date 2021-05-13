package edu.upc.dsa.firefighteradventure.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import edu.upc.dsa.firefighteradventure.models.UserRanking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {
    private List<UserRanking> values;

    private MainActivity mainActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvUsernameRanking;
        public TextView tvPositionScoreRanking;
        public ImageView ivProfilePhotoRanking;
        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvUsernameRanking = view.findViewById(R.id.tvUsernameRanking);
            tvPositionScoreRanking = view.findViewById(R.id.tvPositionScoreRanking);
            ivProfilePhotoRanking = view.findViewById(R.id.ivProfilePhotoRanking);
        }
    }

    public void add(int position, UserRanking userRanking) {
        values.add(position, userRanking);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RankingAdapter(List<UserRanking> myDataset, MainActivity mainActivity) {

        values = myDataset;
        this.mainActivity = mainActivity;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RankingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.ranking_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String username = values.get(position).getUsername();
        final int score = values.get(position).getScore();

        holder.tvUsernameRanking.setText(username);

        int pos = position + 1;

        holder.tvPositionScoreRanking.setText(mainActivity.getString(R.string.position_string)
                + ": " + pos + " - " + mainActivity.getString(R.string.score_string)
                .replaceFirst("💯 ", "")
                + ": " + score);

        Picasso.get().load(values.get(position).getProfile_photo())
                .resize(130, 130)
                .centerCrop(Gravity.CENTER)
                .into(holder.ivProfilePhotoRanking);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.setLoadingData(true);

                Call<UserProfile> user = mainActivity.getUserService()
                        .getUserProfileByUsername(holder.tvUsernameRanking
                                .getText().toString());

                user.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        mainActivity.setLoadingData(false);

                        switch (response.code()) {

                            case 200:

                                UserProfile userProfile = response.body();

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("userProfile", userProfile);

                                Navigation.findNavController(view).navigate(R.id.
                                        profileGeneralFragment, bundle);

                                break;

                            case 404:
                                Toast.makeText(mainActivity, R.string.user_not_exists_string,
                                        Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Navigation.findNavController(view).navigate(R.id
                                        .connectionErrorFragment);
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
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
