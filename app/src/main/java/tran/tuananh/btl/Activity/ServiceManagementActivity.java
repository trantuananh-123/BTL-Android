package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Adapter.ServiceRcvAdapter;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class ServiceManagementActivity extends AppCompatActivity implements View.OnClickListener, ViewHolderListener {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private SearchView searchView;
    private TextView totalService;
    private ImageButton btnBackArrow;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private ServiceRcvAdapter serviceRcvAdapter;
    private ServiceDB serviceDB;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<Service> serviceList = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

//        initView();
//        initData();
//        initListener();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        btnAdd = findViewById(R.id.btnAdd);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        totalService = findViewById(R.id.totalService);
        serviceRcvAdapter = new ServiceRcvAdapter(this);
        serviceRcvAdapter.setViewHolderListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        serviceDB = new ServiceDB(this, firebaseFirestore);
    }

    private void initData() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null && firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        initSpecialist();

    }

    private void initSpecialist() {
        serviceList = new ArrayList<>();
        Task<QuerySnapshot> querySnapshotTask = serviceDB.getAll();
        querySnapshotTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Service service = documentSnapshot.toObject(Service.class);
                        serviceList.add(service);
                    }
                }
                totalService.setText("Service list(" + String.valueOf(serviceList.size()) + ")");
                serviceRcvAdapter.setServiceList(serviceList);
                recyclerView.setAdapter(serviceRcvAdapter);
                progressBar.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        });
    }

    private void initListener() {
        this.btnAdd.setOnClickListener(this);
        this.btnBackArrow.setOnClickListener(this);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Service> tempList = new ArrayList<>();
                for (Service service : serviceList) {
                    if (service.getName().toLowerCase().contains(s.toLowerCase())) {
                        tempList.add(service);
                    }
                }
                serviceRcvAdapter.setServiceList(tempList);
                recyclerView.setAdapter(serviceRcvAdapter);
                totalService.setText("Service list(" + tempList.size() + ")");
                return true;
            }
        });
    }

    @Override
    public void onClickItemRcvHolder(View view, int position) {
        Service service = serviceRcvAdapter.getService(position);
        Intent intent = new Intent(this, UpdateDeleteServiceActivity.class);
        intent.putExtra("firebaseUser", firebaseUser);
        intent.putExtra("service", service);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBackArrow) {
            finish();
        } else if (view == btnAdd) {
            startActivity(new Intent(this, AddServiceActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initData();
        initListener();
    }
}