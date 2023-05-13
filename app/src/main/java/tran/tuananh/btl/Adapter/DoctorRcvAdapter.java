package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Activity.BookingActivity;
import tran.tuananh.btl.Activity.HomeActivity;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.DoctorRcvHolder;
import tran.tuananh.btl.ViewHolder.ServiceRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class DoctorRcvAdapter extends RecyclerView.Adapter<DoctorRcvHolder> {

    private List<User> doctorList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;

    public DoctorRcvAdapter(Context context) {
        this.context = context;
    }

    public void setDoctorList(List<User> doctorList) {
        this.doctorList = doctorList;
        notifyDataSetChanged();
    }

    public User getDoctor(int position) {
        return this.doctorList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public DoctorRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item_rcv, parent, false);
        return new DoctorRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorRcvHolder holder, int position) {
        User doctor = getDoctor(position);
        holder.getName().setText(doctor.getName());
        holder.getSpecialist().setText(doctor.getSpecialist().getName());
        holder.getExperience().setText("So nam kinh nghiem: " + doctor.getExperience());
        if (doctor.getAvatar() != null && !doctor.getAvatar().equals("")) {
            Glide.with(holder.itemView).load(doctor.getAvatar()).into(holder.getAvatar());
        }
        holder.getBtnBookingAppointment().setOnClickListener(view -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("doctor", doctor);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.doctorList.size();
    }
}
