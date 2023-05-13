package tran.tuananh.btl.Fragment.HealthFacilityDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Adapter.ServiceRcvAdapter;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.R;

public class FragmentService extends Fragment {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private SearchView searchView;
    private MaterialTextView totalService;
    private RecyclerView recyclerView;
    private HealthFacility healthFacility;
    private ServiceRcvAdapter serviceRcvAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ServiceDB serviceDB;
    private List<Service> serviceList = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_healthfacility_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        serviceDB = new ServiceDB(getContext(), firebaseFirestore);

        initView(view);
        initData();
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null && firebaseUser == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.searchView);
        totalService = view.findViewById(R.id.totalService);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);
        serviceRcvAdapter = new ServiceRcvAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        initService();
        progressBar.setVisibility(View.GONE);
        progressBarBackground.setVisibility(View.GONE);
    }

    private void initService() {
        if (healthFacility == null) {
            Intent intent = requireActivity().getIntent();
            healthFacility = (HealthFacility) intent.getSerializableExtra("healthFacility");
        }
        serviceList = new ArrayList<>();
        List<Integer> serviceIds = healthFacility.getServiceIds();
        if (serviceIds != null && serviceIds.size() > 0) {
            for (Integer id : serviceIds) {
                Task<QuerySnapshot> serviceTask = serviceDB.getById(id);
                serviceTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                            Service service = documentSnapshot.toObject(Service.class);
                            serviceList.add(service);
                        }
                    }
                    serviceRcvAdapter.setServiceList(serviceList);
                    totalService.setText("Service list(" + serviceList.size() + ")");
                    recyclerView.setAdapter(serviceRcvAdapter);
                });
            }
        } else {
            totalService.setText("Service list(0)");
        }
    }

    private void initListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
}
