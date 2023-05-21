package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.BookingRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class BookingRcvAdapter extends RecyclerView.Adapter<BookingRcvHolder> {

    private List<Booking> bookingList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public BookingRcvAdapter(Context context) {
        this.context = context;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }

    public Booking getBooking(int position) {
        return this.bookingList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public BookingRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item_rcv, parent, false);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        return new BookingRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRcvHolder holder, int position) {
        Booking booking = getBooking(position);
        holder.getExaminationDate().append(booking.getExaminationDate());
        holder.getExaminationHour().append(booking.getExaminationHour());
        firebaseFirestore.collection("specialist").whereEqualTo("id", booking.getSpecialistId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Specialist specialist = documentSnapshot.toObject(Specialist.class);
                        if (specialist != null) {
                            holder.getSpecialist().append(specialist.getName());
                        }
                    }
                }
            }
        });
        firebaseFirestore.collection("healthFacility").whereEqualTo("id", booking.getHealthFacilityId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        HealthFacility healthFacility = documentSnapshot.toObject(HealthFacility.class);
                        if (healthFacility != null) {
                            holder.getHealthFacility().append(healthFacility.getName());
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.bookingList.size();
    }
}
