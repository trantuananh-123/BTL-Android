package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class SpecialistRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView code, name;
    private ViewHolderListener viewHolderListener;

    public SpecialistRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        code = itemView.findViewById(R.id.code);
        name = itemView.findViewById(R.id.name);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickItemRcvHolder(view, getAdapterPosition());
        }
    }

    public TextView getCode() {
        return code;
    }

    public void setCode(TextView code) {
        this.code = code;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
