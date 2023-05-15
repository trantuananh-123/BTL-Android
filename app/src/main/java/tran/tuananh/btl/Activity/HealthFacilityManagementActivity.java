package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Adapter.HealthFacilityRcvAdapter;
import tran.tuananh.btl.Database.HealthFacilityDB;
import tran.tuananh.btl.Database.ProvinceDB;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Province;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class HealthFacilityManagementActivity extends AppCompatActivity implements View.OnClickListener, ViewHolderListener {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private SearchView searchView;
    private TextView spinnerProvince, totalHealthFacility;
    private ImageButton btnBackArrow;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private HealthFacilityRcvAdapter healthFacilityRcvAdapter;
    private BottomSheetDialog dialog;
    private CommonListViewAdapter<Province> adapterProvince;
    private List<Province> provinceList = new ArrayList<>();
    private ProvinceDB provinceDB;
    private Province province = new Province();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private HealthFacilityDB healthFacilityDB;
    private List<HealthFacility> healthFacilityList = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_facility_management);

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
        spinnerProvince = findViewById(R.id.spinnerProvince);
        totalHealthFacility = findViewById(R.id.totalHealthFacility);
        healthFacilityRcvAdapter = new HealthFacilityRcvAdapter(this);
        healthFacilityRcvAdapter.setViewHolderListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        healthFacilityDB = new HealthFacilityDB(this, firebaseFirestore);
        provinceDB = new ProvinceDB(this, firebaseFirestore);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null && firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        initProvince();
        initHealthFacility(null);
    }

    private void initListener() {
        this.spinnerProvince.setOnClickListener(this);
        this.btnAdd.setOnClickListener(this);
        this.btnBackArrow.setOnClickListener(this);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<HealthFacility> tempList = new ArrayList<>();
                for (HealthFacility healthFacility : healthFacilityList) {
                    if (healthFacility.getName().toLowerCase().contains(s.toLowerCase())) {
                        tempList.add(healthFacility);
                    }
                }
                healthFacilityRcvAdapter.setHealthFacilityList(tempList);
                recyclerView.setAdapter(healthFacilityRcvAdapter);
                totalHealthFacility.setText("HealthFacility list(" + tempList.size() + ")");
                return true;
            }
        });
    }

    private void initProvince() {
        // Get All Province
        provinceList = new ArrayList<>();
        Task<QuerySnapshot> provinceTask = provinceDB.getAll();
        provinceTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Province province = documentSnapshot.toObject(Province.class);
                    provinceList.add(province);
                }
            }
            adapterProvince = new CommonListViewAdapter<>(provinceList, this);
        });
    }

    private void initHealthFacility(String provinceCode) {
        healthFacilityList = new ArrayList<>();
        Task<QuerySnapshot> healthFacilityTask = healthFacilityDB.getByProvince(provinceCode);

        healthFacilityTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    HealthFacility healthFacility = documentSnapshot.toObject(HealthFacility.class);
                    healthFacilityList.add(healthFacility);
                }
                healthFacilityRcvAdapter.setHealthFacilityList(healthFacilityList);
                recyclerView.setAdapter(healthFacilityRcvAdapter);
                totalHealthFacility.setText("HealthFacility list(" + healthFacilityList.size() + ")");
            }
            progressBar.setVisibility(View.GONE);
            progressBarBackground.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClickItemRcvHolder(View view, int position) {
        HealthFacility healthFacility = healthFacilityRcvAdapter.getHealthFacility(position);
        Intent intent = new Intent(this, UpdateDeleteHealthFacilityActivity.class);
        intent.putExtra("firebaseUser", firebaseUser);
        intent.putExtra("healthFacility", healthFacility);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == spinnerProvince) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterProvince);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterProvince.getItem(i).getId(), province.getId())) {
                        province = adapterProvince.getItem(i);
                        spinnerProvince.setText(adapterProvince.getItem(i).getName());
                        initHealthFacility(province.getCode());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Province");
            }
            dialog.show();
        } else if (view == btnBackArrow) {
            finish();
        } else if (view == btnAdd) {
            startActivity(new Intent(this, AddHealthFacilityActivity.class));
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