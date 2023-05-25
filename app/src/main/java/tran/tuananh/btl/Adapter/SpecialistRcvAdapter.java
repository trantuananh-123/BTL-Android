package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.SpecialistRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class SpecialistRcvAdapter extends RecyclerView.Adapter<SpecialistRcvHolder> {

    private List<Specialist> specialistList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;

    public SpecialistRcvAdapter(Context context) {
        this.context = context;
    }

    public void setSpecialistList(List<Specialist> specialistList) {
        this.specialistList = specialistList;
        notifyDataSetChanged();
    }

    public Specialist getSpecialist(int position) {
        return this.specialistList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public SpecialistRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialist_item_rcv, parent, false);
        return new SpecialistRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialistRcvHolder holder, int position) {
        Specialist specialist = getSpecialist(position);
        holder.getCode().setText(specialist.getCode());
        holder.getName().setText(specialist.getName());
    }

    @Override
    public int getItemCount() {
        return this.specialistList.size();
    }
}
