package edu.upc.dsa.firefighteradventure.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.fragments.ShopFragment;
import edu.upc.dsa.firefighteradventure.models.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Item> values;
    private MainActivity mainActivity;
    private ShopFragment shopFragment;

    private String destination;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNameShop;
        public TextView tvDescriptionShop;
        public TextView tvPriceShop;
        public TextView tvYouHaveShop;
        public ImageView ivShop;

        public Button btnBuyShop;

        public View view;

        public ViewHolder(View view) {

            super(view);
            this.view = view;
            tvNameShop = view.findViewById(R.id.tvNameShop);
            tvDescriptionShop = view.findViewById(R.id.tvDescriptionShop);
            tvPriceShop = view.findViewById(R.id.tvPriceShop);
            tvYouHaveShop = view.findViewById(R.id.tvYouHave);
            ivShop = view.findViewById(R.id.ivShop);
            btnBuyShop = view.findViewById(R.id.btnBuyShop);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShopAdapter(List<Item> myDataset, ShopFragment shopFragment) {

        values = myDataset;
        this.shopFragment = shopFragment;
        this.destination = destination;

        this.mainActivity = shopFragment.getMainActivity();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.shop_item_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Item item = values.get(position);

        holder.tvNameShop.setText(item.getName());
        holder.tvDescriptionShop.setText(item.getDescription());
        holder.tvPriceShop.setText(mainActivity.getString(R.string.price_string) + ": "
                + item.getPrice());

        holder.tvYouHaveShop.setText(mainActivity.getString(R.string.you_have_string) + ": " +
                shopFragment.getQuantityInventory(values.get(position).getId()));

        Picasso.get().load(mainActivity.getBase_url() + item.getImage())
                .resize(130, 130)
                .centerCrop(Gravity.CENTER)
                .into(holder.ivShop);

        holder.btnBuyShop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                LinearLayout container = new LinearLayout(mainActivity);
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(50, 0, 50,0);

                final NumberPicker numberPicker = new NumberPicker(mainActivity);
                numberPicker.setMaxValue(50);
                numberPicker.setMinValue(1);

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(R.string.how_many_do_you_want_to_buy_string);

                container.addView(numberPicker);
                builder.setView(container);

                builder.setPositiveButton(R.string.buy_string, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        mainActivity.setLoadingData(true);

                        Call<ResponseBody> resp = mainActivity.getShopService()
                                .buyItemById(item.getId(), shopFragment.getId_game(),
                                        numberPicker.getValue());

                        resp.enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call
                                    , Response<ResponseBody> response) {

                                mainActivity.setLoadingData(false);

                                if (response.code() == 200) {

                                    Toast.makeText(mainActivity, mainActivity
                                            .getString(R.string.you_have_bought_string) +
                                            " " + item.getName(), Toast.LENGTH_SHORT).show();

                                    shopFragment.updateCoins(shopFragment.getCoins() -
                                            (item.getPrice() * numberPicker.getValue()));

                                    shopFragment.getGameInventory();

                                } else if (response.code() == 404) {

                                    Toast.makeText(mainActivity, R.string.item_does_not_exist_string
                                            , Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 700) {

                                    Toast.makeText(mainActivity, R.string.game_does_not_exist_string
                                            , Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 701) {

                                    Toast.makeText(mainActivity, R.string.not_enough_coins_string
                                            , Toast.LENGTH_SHORT).show();

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
                    }
                });
                builder.setNegativeButton(R.string.back_string, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                builder.show();

            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
