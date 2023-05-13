package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class HealthFacilityRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView healthFacilityLogo;
    private TextView healthFacilityName, healthFacilityAddress;
    private ViewHolderListener viewHolderListener;

    public HealthFacilityRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        healthFacilityLogo = itemView.findViewById(R.id.healthFacilityLogo);
        healthFacilityName = itemView.findViewById(R.id.healthFacilityName);
        healthFacilityAddress = itemView.findViewById(R.id.healthFacilityAddress);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickItemRcvHolder(view, getAdapterPosition());
        }
    }

    public ImageView getHealthFacilityLogo() {
        return healthFacilityLogo;
    }

    public void setHealthFacilityLogo(ImageView healthFacilityLogo) {
        this.healthFacilityLogo = healthFacilityLogo;
    }

    public TextView getHealthFacilityName() {
        return healthFacilityName;
    }

    public void setHealthFacilityName(TextView healthFacilityName) {
        this.healthFacilityName = healthFacilityName;
    }

    public TextView getHealthFacilityAddress() {
        return healthFacilityAddress;
    }

    public void setHealthFacilityAddress(TextView healthFacilityAddress) {
        this.healthFacilityAddress = healthFacilityAddress;
    }
}
