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

import tran.tuananh.btl.Adapter.NotificationRcvAdapter;
import tran.tuananh.btl.Database.NotificationDB;
import tran.tuananh.btl.Model.Notification;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class FragmentNotification extends Fragment implements ViewHolderListener {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private TextView totalNotification;
    private RecyclerView recyclerView;
    private NotificationRcvAdapter notificationRcvAdapter;
    private List<Notification> notificationList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private NotificationDB notificationDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        notificationRcvAdapter = new NotificationRcvAdapter(getContext());
        notificationRcvAdapter.setViewHolderListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        notificationDB = new NotificationDB(getContext(), firebaseFirestore);
        initData();
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);
        recyclerView = view.findViewById(R.id.recyclerView);
        totalNotification = view.findViewById(R.id.totalNotification);
    }

    private void initData() {
        notificationList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }
        Task<QuerySnapshot> bookingTask = notificationDB.getPatientNotification(firebaseUser.getUid());
        bookingTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Notification notification = documentSnapshot.toObject(Notification.class);
                        notificationList.add(notification);
                    }
                    notificationRcvAdapter.setNotificationList(notificationList);
                    recyclerView.setAdapter(notificationRcvAdapter);
                    totalNotification.setText("Notification list(" + notificationList.size() + ")");
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
