package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class PersonalRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView menuIcon;
    private TextView menuName;
    private ViewHolderListener viewHolderListener;

    public PersonalRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        menuIcon = itemView.findViewById(R.id.menuIcon);
        menuName = itemView.findViewById(R.id.menuName);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickPersonalRcvHolder(view, getAdapterPosition());
        }
    }

    public ImageView getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(ImageView menuIcon) {
        this.menuIcon = menuIcon;
    }

    public TextView getMenuName() {
        return menuName;
    }

    public void setMenuName(TextView menuName) {
        this.menuName = menuName;
    }
}
