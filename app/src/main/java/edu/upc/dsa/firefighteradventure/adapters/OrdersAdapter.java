package edu.upc.dsa.firefighteradventure.adapters;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.firefighteradventure.MainActivity;
import edu.upc.dsa.firefighteradventure.R;
import edu.upc.dsa.firefighteradventure.fragments.OrdersFragment;
import edu.upc.dsa.firefighteradventure.fragments.ShopFragment;
import edu.upc.dsa.firefighteradventure.models.Item;
import edu.upc.dsa.firefighteradventure.models.Orders;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Orders> values;
    private MainActivity mainActivity;
    private OrdersFragment ordersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNameOrder;
        public TextView tvDescriptionOrder;
        public TextView tvPriceOrder;
        public TextView tvBoughtAtOn;
        public ImageView ivOrder;
        public TextView tvQuantityOrder;


        public View view;

        public ViewHolder(View view) {

            super(view);
            this.view = view;

            tvNameOrder = view.findViewById(R.id.tvNameOrder);
            tvDescriptionOrder = view.findViewById(R.id.tvDescriptionOrder);
            tvPriceOrder = view.findViewById(R.id.tvPriceOrder);
            tvBoughtAtOn = view.findViewById(R.id.tvBoughtAtOn);
            tvQuantityOrder = view.findViewById(R.id.tvQuantityOrder);
            ivOrder = view.findViewById(R.id.ivOrder);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrdersAdapter(List<Orders> myDataset, OrdersFragment ordersFragment) {

        values = myDataset;
        this.ordersFragment = ordersFragment;

        this.mainActivity = this.ordersFragment.getMainActivity();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.orders_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Orders order = values.get(position);

        holder.tvNameOrder.setText(mainActivity.getItemById(order.getId_item()).getName());
        holder.tvDescriptionOrder.setText(mainActivity.getItemById(order.getId_item())
                .getDescription());

        holder.tvPriceOrder.setText(mainActivity.getString(R.string.price_string) + ": "
                + mainActivity.getItemById(order.getId_item()).getPrice());

        holder.tvQuantityOrder.setText(mainActivity.getString(R.string.amount_string) + ": "
                + order.getQuantity());

        holder.tvBoughtAtOn.setText(mainActivity.getString(R.string.bought_at_on_string,
                order.getOrderTime(), order.getOrderDate()));

        Picasso.get().load(mainActivity.getBase_url() + mainActivity
                .getItemById(order.getId_item()).getImage())
                .resize(130, 130).centerCrop(Gravity.CENTER)
                .into(holder.ivOrder);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
