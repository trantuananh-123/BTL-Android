package tran.tuananh.btl.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tran.tuananh.btl.R;

public class BookingRcvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView examinationDate, examinationHour, healthFacility, specialist;
    private ViewHolderListener viewHolderListener;

    public BookingRcvHolder(@NonNull View itemView, ViewHolderListener viewHolderListener) {
        super(itemView);
        examinationDate = itemView.findViewById(R.id.examinationDate);
        examinationHour = itemView.findViewById(R.id.examinationHour);
        healthFacility = itemView.findViewById(R.id.healthFacility);
        specialist = itemView.findViewById(R.id.specialist);
        this.viewHolderListener = viewHolderListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (this.viewHolderListener != null) {
            this.viewHolderListener.onClickItemRcvHolder(view, getAdapterPosition());
        }
    }

    public TextView getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(TextView examinationDate) {
        this.examinationDate = examinationDate;
    }

    public TextView getExaminationHour() {
        return examinationHour;
    }

    public void setExaminationHour(TextView examinationHour) {
        this.examinationHour = examinationHour;
    }

    public TextView getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(TextView healthFacility) {
        this.healthFacility = healthFacility;
    }

    public TextView getSpecialist() {
        return specialist;
    }

    public void setSpecialist(TextView specialist) {
        this.specialist = specialist;
    }

    public ViewHolderListener getViewHolderListener() {
        return viewHolderListener;
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }
}
