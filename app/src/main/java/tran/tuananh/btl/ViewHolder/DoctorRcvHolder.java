package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import tran.tuananh.btl.R;

public class DoctorRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView name, specialist, experience;
    private ShapeableImageView avatar;
    private MaterialButton btnBookingAppointment;
    private ViewHolderListener viewHolderListener;

    public DoctorRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        specialist = itemView.findViewById(R.id.specialist);
        experience = itemView.findViewById(R.id.experience);
        avatar = itemView.findViewById(R.id.avatar);
        btnBookingAppointment = itemView.findViewById(R.id.btnBookingAppointment);
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

    public TextView getSpecialist() {
        return specialist;
    }

    public void setSpecialist(TextView specialist) {
        this.specialist = specialist;
    }

    public TextView getExperience() {
        return experience;
    }

    public void setExperience(TextView experience) {
        this.experience = experience;
    }

    public ShapeableImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ShapeableImageView avatar) {
        this.avatar = avatar;
    }

    public MaterialButton getBtnBookingAppointment() {
        return btnBookingAppointment;
    }

    public void setBtnBookingAppointment(MaterialButton btnBookingAppointment) {
        this.btnBookingAppointment = btnBookingAppointment;
    }
}
