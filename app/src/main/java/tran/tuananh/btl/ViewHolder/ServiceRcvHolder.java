package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class ServiceRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView name, price;
    private ViewHolderListener viewHolderListener;

    public ServiceRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickItemRcvHolder(view, getAdapterPosition());
        }
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }
}
