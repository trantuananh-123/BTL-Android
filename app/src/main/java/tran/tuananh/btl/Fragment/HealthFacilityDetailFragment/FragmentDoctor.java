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
import tran.tuananh.btl.Adapter.DoctorRcvAdapter;
import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Database.UserDB;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class FragmentDoctor extends Fragment {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private SearchView searchView;
    private MaterialTextView totalDoctor;
    private RecyclerView recyclerView;
    private HealthFacility healthFacility;
    private DoctorRcvAdapter doctorRcvAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private UserDB userDB;
    private SpecialistDB specialistDB;
    private List<User> doctorList = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_healthfacility_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userDB = new UserDB(getContext(), firebaseFirestore);
        specialistDB = new SpecialistDB(getContext(), firebaseFirestore);

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
        totalDoctor = view.findViewById(R.id.totalDoctor);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);
        doctorRcvAdapter = new DoctorRcvAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        initDoctor();
        progressBar.setVisibility(View.GONE);
        progressBarBackground.setVisibility(View.GONE);
    }

    private void initDoctor() {
        if (healthFacility == null) {
            Intent intent = requireActivity().getIntent();
            healthFacility = (HealthFacility) intent.getSerializableExtra("healthFacility");
        }
        doctorList = new ArrayList<>();
        if (healthFacility.getId() != null) {
            Task<QuerySnapshot> serviceTask = userDB.getDoctorByHealthFacility(healthFacility.getId());
            serviceTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> userSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot userSnapshot : userSnapshotList) {
                        User doctor = userSnapshot.toObject(User.class);
                        if (doctor != null) {
                            Task<QuerySnapshot> specialistTask = specialistDB.getById(doctor.getSpecialistId());
                            specialistTask.addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    List<DocumentSnapshot> specialistSnapshotList = task1.getResult().getDocuments();
                                    for (DocumentSnapshot specialistSnapshot : specialistSnapshotList) {
                                        Specialist specialist = specialistSnapshot.toObject(Specialist.class);
                                        doctor.setSpecialist(specialist);
                                        doctorList.add(doctor);
                                    }
                                    doctorRcvAdapter.setDoctorList(doctorList);
                                    totalDoctor.setText("Doctor list(" + doctorList.size() + ")");
                                    recyclerView.setAdapter(doctorRcvAdapter);
                                }
                            });
                        }
                    }
                }
            });

        } else {
            totalDoctor.setText("Doctor list(0)");
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
                List<User> tempList = new ArrayList<>();
                for (User doctor : doctorList) {
                    if (doctor.getName().toLowerCase().contains(s.toLowerCase())) {
                        tempList.add(doctor);
                    }
                }
                doctorRcvAdapter.setDoctorList(tempList);
                recyclerView.setAdapter(doctorRcvAdapter);
                totalDoctor.setText("Doctor list(" + tempList.size() + ")");
                return true;
            }
        });
    }
}
