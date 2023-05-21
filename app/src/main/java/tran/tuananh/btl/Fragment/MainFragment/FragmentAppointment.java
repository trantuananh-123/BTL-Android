package tran.tuananh.btl.Fragment.MainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Adapter.BookingRcvAdapter;
import tran.tuananh.btl.Database.BookingDB;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class FragmentAppointment extends Fragment implements ViewHolderListener {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private TextView totalAppointment;
    private RecyclerView recyclerView;
    private BookingRcvAdapter bookingRcvAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private BookingDB bookingDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        bookingRcvAdapter = new BookingRcvAdapter(getContext());
        bookingRcvAdapter.setViewHolderListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        bookingDB = new BookingDB(getContext(), firebaseFirestore);
        initData();
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);
        recyclerView = view.findViewById(R.id.recyclerView);
        totalAppointment = view.findViewById(R.id.totalAppointment);
    }

    private void initData() {
        bookingList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }
        Task<QuerySnapshot> bookingTask = bookingDB.getByUserId(firebaseUser.getUid());
        bookingTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        bookingList.add(booking);
                    }
                    bookingRcvAdapter.setBookingList(bookingList);
                    recyclerView.setAdapter(bookingRcvAdapter);
                    totalAppointment.setText("Appointment list(" + bookingList.size() + ")");
                }
                progressBar.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClickItemRcvHolder(View view, int position) {

    }
}
