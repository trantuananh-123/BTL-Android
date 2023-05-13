package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ServiceRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class ServiceRcvAdapter extends RecyclerView.Adapter<ServiceRcvHolder> {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");

    private List<Service> serviceList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;

    public ServiceRcvAdapter(Context context) {
        this.context = context;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }

    public Service getService(int position) {
        return this.serviceList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public ServiceRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_rcv, parent, false);
        return new ServiceRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRcvHolder holder, int position) {
        Service service = getService(position);
        holder.getName().setText(service.getName());
        holder.getPrice().setText(NUMBER_FORMAT.format(service.getPrice()) + "đ");
    }

    @Override
    public int getItemCount() {
        return this.serviceList.size();
    }
}
