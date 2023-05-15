package tran.tuananh.btl.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Database.DistrictDB;
import tran.tuananh.btl.Database.ProvinceDB;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Database.WardDB;
import tran.tuananh.btl.Model.District;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Province;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.Model.Ward;
import tran.tuananh.btl.R;
import tran.tuananh.btl.Util.FnCommon;

public class UpdateDeleteHealthFacilityActivity extends AppCompatActivity implements View.OnClickListener {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ProgressBar progressBar;
    private View progressBarBackground;

    private ImageButton btnBackArrow;
    private ImageView logo;
    private Uri logoUri;
    private EditText name, code, address, email, phone, fanpage, website;
    private TextView spinnerProvince, spinnerDistrict, spinnerWard, spinnerSpecialist, spinnerService;
    private MaterialButton btnSave;
    private BottomSheetDialog dialog;
    private FirebaseFirestore firebaseFirestore;
    private ProvinceDB provinceDB;
    private DistrictDB districtDB;
    private WardDB wardDB;
    private SpecialistDB specialistDB;
    private ServiceDB serviceDB;
    private CommonListViewAdapter<Province> adapterProvince;
    private CommonListViewAdapter<District> adapterDistrict;
    private CommonListViewAdapter<Ward> adapterWard;
    private CommonListViewAdapter<Specialist> adapterSpecialist;
    private CommonListViewAdapter<Service> adapterService;
    private List<Province> provinceList = new ArrayList<>();
    private Province province = new Province();
    private List<District> districtList = new ArrayList<>();
    private District district = new District();
    private List<Ward> wardList = new ArrayList<>();
    private Ward ward = new Ward();
    private List<Specialist> specialistList = new ArrayList<>();
    private List<Integer> specialistIndexList = new ArrayList<>();
    private List<String> specialistIdList = new ArrayList<>();
    private boolean[] isSelectedSpecialistList;
    private List<String> specialistNameList = new ArrayList<>();
    //    private Specialist specialist = new Specialist();
    private List<Service> serviceList = new ArrayList<>();
    private List<Integer> serviceIndexList = new ArrayList<>();
    private List<String> serviceIdList = new ArrayList<>();
    private boolean[] isSelectedServiceList;
    private List<String> serviceNameList = new ArrayList<>();
    private HealthFacility healthFacility = new HealthFacility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_health_facility);
        firebaseFirestore = FirebaseFirestore.getInstance();
        provinceDB = new ProvinceDB(this, firebaseFirestore);
        districtDB = new DistrictDB(this, firebaseFirestore);
        wardDB = new WardDB(this, firebaseFirestore);
        specialistDB = new SpecialistDB(this, firebaseFirestore);
        serviceDB = new ServiceDB(this, firebaseFirestore);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        initData();
        initListener();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        fanpage = findViewById(R.id.fanpage);
        website = findViewById(R.id.website);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerDistrict.setEnabled(false);
        spinnerDistrict.setBackground(ContextCompat.getDrawable(this, R.drawable.search_spinner_disabled));
        spinnerWard = findViewById(R.id.spinnerWard);
        spinnerWard.setEnabled(false);
        spinnerWard.setBackground(ContextCompat.getDrawable(this, R.drawable.search_spinner_disabled));
        spinnerSpecialist = findViewById(R.id.spinnerSpecialist);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerService.setEnabled(false);
        spinnerService.setBackground(ContextCompat.getDrawable(this, R.drawable.search_spinner_disabled));
        btnSave = findViewById(R.id.btnSave);
    }

    private void initData() {
        Intent intent = getIntent();
        healthFacility = (HealthFacility) intent.getSerializableExtra("healthFacility");
        if (healthFacility != null) {
            name.setText(healthFacility.getName());
            code.setText(healthFacility.getCode());
            if (healthFacility.getProvince() != null) {
                province = healthFacility.getProvince();
                spinnerProvince.setText(province.getName());
                initDistrict();
                district = new District();
                spinnerDistrict.setEnabled(true);
                spinnerDistrict.setText("");
                spinnerDistrict.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
            }
            if (healthFacility.getDistrict() != null) {
                district = healthFacility.getDistrict();
                spinnerDistrict.setText(district.getName());
                initWard();
                ward = new Ward();
                spinnerWard.setEnabled(true);
                spinnerWard.setText("");
                spinnerWard.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
            }
            if (healthFacility.getWard() != null) {
                ward = healthFacility.getWard();
                spinnerWard.setText(ward.getName());
            }
            address.setText(healthFacility.getAddress());
            email.setText(healthFacility.getEmail());
            phone.setText(healthFacility.getPhone());
            fanpage.setText(healthFacility.getFanpage());
            website.setText(healthFacility.getWebsite());
        }
        initProvince();
        initSpecialist();
//        initDistrict();
//        initWard();
//        initSpecialist();
    }

    private void initListener() {
        this.logo.setOnClickListener(this);
        this.spinnerProvince.setOnClickListener(this);
        this.spinnerDistrict.setOnClickListener(this);
        this.spinnerWard.setOnClickListener(this);
        this.spinnerSpecialist.setOnClickListener(this);
        this.spinnerService.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
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

    private void initDistrict() {
        // Get All District
        districtList = new ArrayList<>();
        Task<QuerySnapshot> districtTask = districtDB.getAllByProvince(province.getCode());
        districtTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> districtSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot districtSnapshot : districtSnapshotList) {
                    District district = districtSnapshot.toObject(District.class);
                    districtList.add(district);
                }
            }
            adapterDistrict = new CommonListViewAdapter<>(districtList, this);
        });
    }

    private void initWard() {
        // Get All Ward
        wardList = new ArrayList<>();
        Task<QuerySnapshot> wardTask = wardDB.getAllByProvinceAndDistrict(province.getCode(), district.getCode());
        wardTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> wardSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot wardSnapshot : wardSnapshotList) {
                    Ward ward = wardSnapshot.toObject(Ward.class);
                    wardList.add(ward);
                }
            }
            adapterWard = new CommonListViewAdapter<>(wardList, this);
        });
    }

    private void initSpecialist() {
        // Get All Specialist
        specialistList = new ArrayList<>();
        isSelectedSpecialistList = new boolean[specialistList.size()];
        specialistIdList.clear();
        specialistIndexList.clear();
        specialistNameList.clear();

        if (healthFacility.getSpecialistIds() != null && healthFacility.getSpecialistIds().size() > 0) {
            specialistIdList = healthFacility.getSpecialistIds();
        }

        Task<QuerySnapshot> specialistTask = specialistDB.getAll();
        specialistTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> specialistSnapshotList = task.getResult().getDocuments();
                StringBuilder stringBuilder = new StringBuilder();
                for (DocumentSnapshot specialistSnapshot : specialistSnapshotList) {
                    Specialist specialist = specialistSnapshot.toObject(Specialist.class);
                    if (specialist != null) {
                        specialistList.add(specialist);
                        if (specialistIdList.contains(specialist.getId())) {
                            if (!stringBuilder.toString().equals("")) {
                                stringBuilder.append("; ");
                            }
                            stringBuilder.append(specialist.getName());
                            spinnerSpecialist.setText(stringBuilder.toString());
                            specialistNameList.add(specialist.getName());
                            specialistIndexList.add(specialistList.indexOf(specialist));
                        }
                    }
                }
                spinnerService.setEnabled(true);
                spinnerService.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
                initService();
            }
            adapterSpecialist = new CommonListViewAdapter<>(specialistList, this);
            isSelectedSpecialistList = new boolean[specialistList.size()];
            for (int index = 0; index < specialistList.size(); index++) {
                if (specialistIdList.contains(specialistList.get(index).getId())) {
                    isSelectedSpecialistList[index] = true;
                }
            }
        });
    }

    private void initService() {
        // Get All Service
        serviceList = new ArrayList<>();
        isSelectedServiceList = new boolean[serviceList.size()];
        serviceIdList.clear();
        serviceIndexList.clear();
        serviceNameList.clear();
        spinnerService.setText("");

        if (healthFacility.getServiceIds() != null && healthFacility.getServiceIds().size() > 0) {
            serviceIdList = healthFacility.getServiceIds();
        }

        Task<QuerySnapshot> serviceTask = serviceDB.getBySpecialist(specialistIdList.size() == 0 ? healthFacility.getSpecialistIds() : specialistIdList);
        serviceTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                StringBuilder stringBuilder = new StringBuilder();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Service service = documentSnapshot.toObject(Service.class);
                    if (service != null) {
                        serviceList.add(service);
                        if (!serviceNameList.contains(service.getName())) {
                            serviceNameList.add(service.getName());
                            serviceIndexList.add(serviceList.indexOf(service));
                        }
                        if (serviceIdList.contains(service.getId())) {
                            if (!stringBuilder.toString().equals("")) {
                                stringBuilder.append("; ");
                            }
                            stringBuilder.append(service.getName());
                            spinnerService.setText(stringBuilder.toString());
                        }
                    }
                }
            }
            adapterService = new CommonListViewAdapter<>(serviceList, this);
            isSelectedServiceList = new boolean[serviceList.size()];
            for (int index = 0; index < serviceList.size(); index++) {
                if (serviceIdList.contains(serviceList.get(index).getId())) {
                    isSelectedServiceList[index] = true;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == logo) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        } else if (view == spinnerProvince) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterProvince);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterProvince.getItem(i).getId(), province.getId())) {
                        province = adapterProvince.getItem(i);
                        spinnerProvince.setText(adapterProvince.getItem(i).getName());

                        initDistrict();
                        district = new District();
                        spinnerDistrict.setEnabled(true);
                        spinnerDistrict.setText("");
                        spinnerDistrict.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
                        ward = new Ward();
                        spinnerWard.setEnabled(false);
                        spinnerWard.setText("");
                        spinnerWard.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_disabled));

                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Province");
            }
            dialog.show();
        } else if (view == spinnerDistrict) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterDistrict);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterDistrict.getItem(i).getId(), district.getId())) {
                        district = adapterDistrict.getItem(i);
                        spinnerDistrict.setText(adapterDistrict.getItem(i).getName());
                        initWard();
                        ward = new Ward();
                        spinnerWard.setEnabled(true);
                        spinnerWard.setText("");
                        spinnerWard.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose District");
            }
            dialog.show();
        } else if (view == spinnerWard) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterWard);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterWard.getItem(i).getId(), ward.getId())) {
                        ward = adapterWard.getItem(i);
                        spinnerWard.setText(adapterWard.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Ward");
            }
            dialog.show();
        } else if (view == spinnerSpecialist) {
            final int[] count = {0};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Specialist");
            builder.setCancelable(false);

            String[] tempList = new String[specialistNameList.size()];
            builder.setMultiChoiceItems(specialistNameList.toArray(tempList), isSelectedSpecialistList, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b) {
                        specialistIndexList.add(i);
                        count[0]++;
                    } else {
                        specialistIndexList.removeIf(integer -> integer == i);
                        count[0]--;
                    }
                }
            }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder stringBuilder = new StringBuilder();
                    specialistIdList = new ArrayList<>();
                    for (int index = 0; index < specialistIndexList.size(); index++) {
                        Specialist selectedSpecialist = specialistList.get(specialistIndexList.get(index));
                        stringBuilder.append(selectedSpecialist.getName());
                        if (!specialistIdList.contains(selectedSpecialist.getId())) {
                            specialistIdList.add(selectedSpecialist.getId());
                        }
                        if (index != specialistIndexList.size() - 1) {
                            stringBuilder.append("; ");
                        }
                        spinnerSpecialist.setText(stringBuilder.toString());
                    }
                    if (isSelectedServiceList != null) {
                        for (int index2 = 0; index2 < isSelectedServiceList.length; index2++) {
                            isSelectedServiceList[index2] = false;
                            serviceIdList.clear();
                            serviceIndexList.clear();
                            spinnerService.setText("");
                        }
                    }
                    initService();
                    spinnerService.setEnabled(true);
                    spinnerService.setBackground(ContextCompat.getDrawable(UpdateDeleteHealthFacilityActivity.this, R.drawable.search_spinner_enabled));
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    while (count[0]-- > 0) {
                        specialistIndexList.remove(specialistIndexList.size() - 1);
                    }
                    dialogInterface.dismiss();
                }
            }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int index = 0; index < isSelectedSpecialistList.length; index++) {
                        isSelectedSpecialistList[index] = false;
                        specialistIdList.clear();
                        specialistIndexList.clear();
                        spinnerSpecialist.setText("");
                    }
                }
            });
            builder.show();
        } else if (view == spinnerService) {
            final int[] count = {0};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Service");
            builder.setCancelable(false);

            String[] tempList = new String[serviceNameList.size()];
            builder.setMultiChoiceItems(serviceNameList.toArray(tempList), isSelectedServiceList, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b) {
                        if (!serviceIndexList.contains(i)) {
                            serviceIndexList.add(i);
                            count[0]++;
                        }
                    } else {
                        serviceIndexList.removeIf(integer -> integer == i);
                        count[0]--;
                    }
                }
            }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder stringBuilder = new StringBuilder();
                    serviceIdList = new ArrayList<>();
                    for (int index = 0; index < serviceIndexList.size(); index++) {
                        Service selectedService = serviceList.get(serviceIndexList.get(index));
                        stringBuilder.append(selectedService.getName());
                        if (!serviceIdList.contains(selectedService.getId())) {
                            serviceIdList.add(selectedService.getId());
                        }
                        if (index != serviceIndexList.size() - 1) {
                            stringBuilder.append("; ");
                        }
                        spinnerService.setText(stringBuilder.toString());
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    while (count[0]-- > 0) {
                        serviceIndexList.remove(serviceIndexList.size() - 1);
                    }
                    dialogInterface.dismiss();
                }
            }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int index = 0; index < isSelectedServiceList.length; index++) {
                        isSelectedServiceList[index] = false;
                        serviceIdList.clear();
                        serviceIndexList.clear();
                        spinnerService.setText("");
                    }
                }
            });
            builder.show();
        } else if (view == btnSave) {
            String name = "", code = "", email = "";
            name = String.valueOf(this.name.getText());
            code = String.valueOf(this.code.getText());
            email = String.valueOf(this.email.getText());
            if (!validate(name, code, email)) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarBackground.setVisibility(View.VISIBLE);

                HashMap<String, Object> hashMap = new LinkedHashMap<>();
                hashMap.put("id", healthFacility.getId());
                hashMap.put("name", name);
                hashMap.put("code", code);
                hashMap.put("email", email);
                hashMap.put("province", province);
                hashMap.put("district", district);
                hashMap.put("ward", ward);
                hashMap.put("address", address.getText().toString());
                hashMap.put("phone", phone.getText().toString());
                hashMap.put("fanpage", fanpage.getText().toString());
                hashMap.put("website", website.getText().toString());
                hashMap.put("specialistIds", specialistIdList);
                hashMap.put("serviceIds", serviceIdList);
                firebaseFirestore.collection("healthFacility").document(healthFacility.getId()).set(hashMap)
                        .addOnCompleteListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            FancyToast.makeText(UpdateDeleteHealthFacilityActivity.this, "Update healthFacility successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
//                            startActivity(new Intent(Add.this, LoginActivity.class));
                        })
                        .addOnFailureListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            FancyToast.makeText(UpdateDeleteHealthFacilityActivity.this, "Update healthFacility failed!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            logoUri = data.getData();
            logo.setImageURI(logoUri);
        }
    }

    private boolean validate(String name, String code, String email) {
        if (TextUtils.isEmpty(name)) {
            FancyToast.makeText(this, "Name is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            return true;
        }

        if (TextUtils.isEmpty(code)) {
            FancyToast.makeText(this, "Code is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            return true;
        }

        if (TextUtils.isEmpty(email)) {
            FancyToast.makeText(this, "Email is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            return true;
        } else if (!validateEmail(email)) {
            FancyToast.makeText(this, "Invalid email", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            return true;
        }

        return false;
    }

    private boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }
}