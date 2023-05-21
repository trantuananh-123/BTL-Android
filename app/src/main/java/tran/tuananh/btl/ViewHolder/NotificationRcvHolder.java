package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class NotificationRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView message;
    private ViewHolderListener viewHolderListener;

    public NotificationRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickItemRcvHolder(view, getAdapterPosition());
        }
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public ViewHolderListener getViewHolderListener() {
        return viewHolderListener;
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }
}
