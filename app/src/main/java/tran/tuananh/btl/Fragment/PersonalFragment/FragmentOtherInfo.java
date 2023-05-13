package tran.tuananh.btl.Fragment.PersonalFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import tran.tuananh.btl.Activity.HomeActivity;
import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Database.EthnicityDB;
import tran.tuananh.btl.Database.JobDB;
import tran.tuananh.btl.Database.NationalityDB;
import tran.tuananh.btl.Database.ReligionDB;
import tran.tuananh.btl.Model.Ethnicity;
import tran.tuananh.btl.Model.Job;
import tran.tuananh.btl.Model.Nationality;
import tran.tuananh.btl.Model.Religion;
import tran.tuananh.btl.R;

public class FragmentOtherInfo extends Fragment implements View.OnClickListener {

    private TextInputLayout inputHealthInsuranceLayout, inputStartDateLayout, inputEndDateLayout;
    private EditText inputHealthInsurance, inputStartDate, inputEndDate;
    private TextView spinnerNationality, spinnerEthnicity, spinnerReligion, spinnerJob;
    private BottomSheetDialog dialog;
    private ProgressBar progressBar;
    private View progressBarBackground;
    private MaterialButton btnSave;
    private CommonListViewAdapter<Nationality> adapterNationality;
    private CommonListViewAdapter<Ethnicity> adapterEthnicity;
    private CommonListViewAdapter<Religion> adapterReligion;
    private CommonListViewAdapter<Job> adapterJob;
    private List<Nationality> nationalityList = new ArrayList<>();
    private List<Ethnicity> ethnicityList = new ArrayList<>();
    private List<Religion> religionList = new ArrayList<>();
    private List<Job> jobList = new ArrayList<>();
    private NationalityDB nationalityDB;
    private EthnicityDB ethnicityDB;
    private ReligionDB religionDB;
    private JobDB jobDB;
    private Nationality nationality;
    private Ethnicity ethnicity;
    private Religion religion;
    private Job job;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_other_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        nationalityDB = new NationalityDB(getContext(), firebaseFirestore);
        ethnicityDB = new EthnicityDB(getContext(), firebaseFirestore);
        religionDB = new ReligionDB(getContext(), firebaseFirestore);
        jobDB = new JobDB(getContext(), firebaseFirestore);

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
        inputHealthInsuranceLayout = view.findViewById(R.id.inputHealthInsuranceLayout);
        inputHealthInsurance = inputHealthInsuranceLayout.getEditText();
        inputStartDateLayout = view.findViewById(R.id.inputStartDateLayout);
        inputStartDate = inputStartDateLayout.getEditText();
        inputEndDateLayout = view.findViewById(R.id.inputEndDateLayout);
        inputEndDate = inputEndDateLayout.getEditText();
        spinnerNationality = view.findViewById(R.id.spinnerNationality);
        spinnerEthnicity = view.findViewById(R.id.spinnerEthnicity);
        spinnerReligion = view.findViewById(R.id.spinnerReligion);
        spinnerJob = view.findViewById(R.id.spinnerJob);
        btnSave = view.findViewById(R.id.btnSave);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);

        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }
        // Fill user information
        firebaseFirestore.collection("user").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        inputHealthInsurance.setText((CharSequence) documentSnapshot.get("healthInsurance"));
                        inputStartDate.setText((CharSequence) documentSnapshot.get("healthInsuranceStartDate"));
                        inputEndDate.setText((CharSequence) documentSnapshot.get("healthInsuranceEndDate"));
                        Gson gson = new Gson();
                        String jsonNationality = gson.toJson(documentSnapshot.get("nationality"));
                        nationality = gson.fromJson(jsonNationality, Nationality.class);
                        String jsonEthnicity = gson.toJson(documentSnapshot.get("ethnicity"));
                        ethnicity = gson.fromJson(jsonEthnicity, Ethnicity.class);
                        String jsonReligion = gson.toJson(documentSnapshot.get("religion"));
                        religion = gson.fromJson(jsonReligion, Religion.class);
                        String jsonJob = gson.toJson(documentSnapshot.get("job"));
                        job = gson.fromJson(jsonJob, Job.class);
                        spinnerNationality.setText(nationality != null ? nationality.getName() : "");
                        spinnerEthnicity.setText(ethnicity != null ? ethnicity.getName() : "");
                        spinnerReligion.setText(religion != null ? religion.getName() : "");
                        spinnerJob.setText(job != null ? job.getName() : "");
                    }
                });
        initNationality();
        initEthnicity();
        initReligion();
        initJob();
    }

    private void initNationality() {
        // Get All Nationality
        nationalityList = new ArrayList<>();
        Task<QuerySnapshot> nationalityTask = nationalityDB.getAll();
        nationalityTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Nationality nationality = documentSnapshot.toObject(Nationality.class);
                    nationalityList.add(nationality);
                }
            }
            adapterNationality = new CommonListViewAdapter<>(nationalityList, getContext());
        });
    }

    private void initEthnicity() {
        // Get All Ethnicity
        ethnicityList = new ArrayList<>();
        Task<QuerySnapshot> ethnicityTask = ethnicityDB.getAll();
        ethnicityTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Ethnicity ethnicity = documentSnapshot.toObject(Ethnicity.class);
                    ethnicityList.add(ethnicity);
                }
            }
            adapterEthnicity = new CommonListViewAdapter<>(ethnicityList, getContext());
        });
    }

    private void initReligion() {
        // Get All Religion
        religionList = new ArrayList<>();
        Task<QuerySnapshot> religionTask = religionDB.getAll();
        religionTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Religion religion = documentSnapshot.toObject(Religion.class);
                    religionList.add(religion);
                }
            }
            adapterReligion = new CommonListViewAdapter<>(religionList, getContext());
        });
    }

    private void initJob() {
        // Get All Job
        jobList = new ArrayList<>();
        Task<QuerySnapshot> jobTask = jobDB.getAll();
        jobTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Job job = documentSnapshot.toObject(Job.class);
                    jobList.add(job);
                }
            }
            adapterJob = new CommonListViewAdapter<>(jobList, getContext());
        });
    }

    private void initListener() {
        this.spinnerNationality.setOnClickListener(this);
        this.spinnerEthnicity.setOnClickListener(this);
        this.spinnerReligion.setOnClickListener(this);
        this.spinnerJob.setOnClickListener(this);
        this.inputStartDate.setOnClickListener(this);
        this.inputEndDate.setOnClickListener(this);
        this.inputStartDateLayout.setEndIconOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputStartDate.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        });
        this.inputEndDateLayout.setEndIconOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputEndDate.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        });
        this.btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == spinnerNationality) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterNationality);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (nationality == null || !Objects.equals(adapterNationality.getItem(i).getId(), nationality.getId())) {
                        nationality = adapterNationality.getItem(i);
                        spinnerNationality.setText(adapterNationality.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Nationality");
            }
            dialog.show();
        } else if (view == spinnerEthnicity) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterEthnicity);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (ethnicity == null || !Objects.equals(adapterEthnicity.getItem(i).getId(), ethnicity.getId())) {
                        ethnicity = adapterEthnicity.getItem(i);
                        spinnerEthnicity.setText(adapterEthnicity.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Ethnicity");
            }
            dialog.show();
        } else if (view == spinnerReligion) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterReligion);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (religion == null || !Objects.equals(adapterReligion.getItem(i).getId(), religion.getId())) {
                        religion = adapterReligion.getItem(i);
                        spinnerReligion.setText(adapterReligion.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Religion");
            }
            dialog.show();
        } else if (view == spinnerJob) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterJob);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (job == null || !Objects.equals(adapterJob.getItem(i).getId(), job.getId())) {
                        job = adapterJob.getItem(i);
                        spinnerJob.setText(adapterJob.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Job");
            }
            dialog.show();
        } else if (view == inputStartDate) {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputStartDate.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        } else if (view == inputEndDate) {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputEndDate.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        } else if (view == btnSave) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarBackground.setVisibility(View.VISIBLE);

            String startDate = inputStartDate.getText().toString();
            String endDate = inputEndDate.getText().toString();

            if (!startDate.equals("") && !startDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                inputStartDateLayout.setError("Invalid date(dd/mm/yyyy)");
            }
            if (!endDate.equals("") && !endDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                inputEndDateLayout.setError("Invalid date(dd/mm/yyyy)");
            }

            if (inputStartDateLayout.getError() == null && inputEndDateLayout.getError() == null) {
                HashMap<String, Object> hashMap = new LinkedHashMap<>();
                hashMap.put("id", firebaseUser.getUid());
                hashMap.put("healthInsurance", inputHealthInsurance.getText().toString().equals("") ? null : inputHealthInsurance.getText().toString());
                hashMap.put("healthInsuranceStartDate", startDate.equals("") ? null : startDate);
                hashMap.put("healthInsuranceEndDate", endDate.equals("") ? null : endDate);
                hashMap.put("nationality", nationality);
                hashMap.put("ethnicity", ethnicity);
                hashMap.put("religion", religion);
                hashMap.put("job", job);

                firebaseFirestore.collection("user").document(firebaseUser.getUid()).update(hashMap)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.putExtra("menuPosition", 3);
                            FancyToast.makeText(getContext(), "Update info successfully !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            FancyToast.makeText(getContext(), "Update info failed.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        });
            } else {
                progressBar.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        }
    }
}
