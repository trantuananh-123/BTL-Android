package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.Notification;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.NotificationRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class NotificationRcvAdapter extends RecyclerView.Adapter<NotificationRcvHolder> {

    private List<Notification> notificationList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public NotificationRcvAdapter(Context context) {
        this.context = context;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    public Notification getNotification(int position) {
        return this.notificationList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public NotificationRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_rcv, parent, false);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        return new NotificationRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRcvHolder holder, int position) {
        Notification notification = getNotification(position);
        holder.getMessage().setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return this.notificationList.size();
    }
}
