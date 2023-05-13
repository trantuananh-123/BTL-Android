package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.HealthFacilityRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class HealthFacilityRcvAdapter extends RecyclerView.Adapter<HealthFacilityRcvHolder> {

    private List<HealthFacility> healthFacilityList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;

    public HealthFacilityRcvAdapter(Context context) {
        this.context = context;
    }

    public void setHealthFacilityList(List<HealthFacility> healthFacilityList) {
        this.healthFacilityList = healthFacilityList;
        notifyDataSetChanged();
    }

    public HealthFacility getHealthFacility(int position) {
        return this.healthFacilityList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public HealthFacilityRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_facility_item_rcv, parent, false);
        return new HealthFacilityRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthFacilityRcvHolder holder, int position) {
        HealthFacility healthFacility = getHealthFacility(position);
        holder.getHealthFacilityName().setText(healthFacility.getName());
        holder.getHealthFacilityAddress().setText(healthFacility.getAddress());
        if (healthFacility.getImage() != null) {
            Glide.with(holder.itemView).load(healthFacility.getImage()).into(holder.getHealthFacilityLogo());
        }
    }

    @Override
    public int getItemCount() {
        return this.healthFacilityList.size();
    }
}
