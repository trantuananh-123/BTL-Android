package tran.tuananh.btl.Fragment.BookingFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Adapter.CommonGridViewAdapter;
import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Database.BookingDB;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Database.UserDB;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class FragmentExaminationInfo extends Fragment implements View.OnClickListener {
    private FragmentDataListener fragmentDataListener;
    private TextInputLayout inputFullNameLayout, inputSymptomLayout, inputExaminationDateLayout;
    private EditText inputFullName, inputSymptom, inputExaminationDate;
    private TextView healthFacilityName, spinnerService, spinnerDoctor, spinnerSpecialist, inputExaminationHour;
    private BottomSheetDialog dialog1, dialog2;
    private MaterialButton btnNext;
    private List<Service> serviceList = new ArrayList<>();
    private List<Service> selectedServiceList = new ArrayList<>();
    private List<Integer> serviceIndexList = new ArrayList<>();
    private List<String> serviceIdList = new ArrayList<>();
    private boolean[] isSelectedServiceList;
    private List<String> serviceNameList = new ArrayList<>();
    private List<Specialist> specialistList = new ArrayList<>();
    private List<User> doctorList = new ArrayList<>();
    private List<String> examinationHourList = new ArrayList<>();
    private List<Booking> existedBookingList = new ArrayList<>();
    private CommonListViewAdapter<Specialist> adapterSpecialist;
    private CommonListViewAdapter<Service> adapterService;
    private CommonListViewAdapter<User> adapterDoctor;
    private ServiceDB serviceDB;
    private SpecialistDB specialistDB;
    private UserDB userDB;
    private BookingDB bookingDB;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private HealthFacility healthFacility;
    private String selectedHour = "";
    private Specialist specialist = new Specialist();
    private User doctor = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_examination_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        serviceDB = new ServiceDB(getContext(), firebaseFirestore);
        specialistDB = new SpecialistDB(getContext(), firebaseFirestore);
        userDB = new UserDB(getContext(), firebaseFirestore);
        bookingDB = new BookingDB(getContext(), firebaseFirestore);

        if (getArguments() != null) {
            healthFacility = (HealthFacility) getArguments().getSerializable("healthFacility");
        }

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
        btnNext = view.findViewById(R.id.btnNext);
        healthFacilityName = view.findViewById(R.id.healthFacilityName);
        inputFullNameLayout = view.findViewById(R.id.inputFullNameLayout);
        inputSymptomLayout = view.findViewById(R.id.inputSymptomLayout);
        inputExaminationDateLayout = view.findViewById(R.id.inputExaminationDateLayout);
        inputFullName = view.findViewById(R.id.inputFullName);
        inputSymptom = view.findViewById(R.id.inputSymptom);
        inputExaminationDate = view.findViewById(R.id.inputExaminationDate);
        inputExaminationHour = view.findViewById(R.id.inputExaminationHour);
        inputExaminationHour.setEnabled(false);
        inputExaminationHour.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_disabled));
        spinnerService = view.findViewById(R.id.spinnerService);
        spinnerDoctor = view.findViewById(R.id.spinnerDoctor);
        spinnerDoctor.setEnabled(false);
        spinnerDoctor.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_disabled));
        spinnerSpecialist = view.findViewById(R.id.spinnerSpecialist);
        dialog1 = new BottomSheetDialog(getContext());
        dialog1.setContentView(R.layout.dialog_searchable_spinner);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2 = new BottomSheetDialog(getContext());
        dialog2.setContentView(R.layout.dialog_select_time_spinner);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        if (healthFacility != null) {
            healthFacilityName.setText("HealthFacility Name: " + healthFacility.getName());
            if (firebaseUser == null) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
            inputFullName.setText(firebaseUser.getDisplayName());
            initSpecialist();
            initDoctor();
            initService();

            if (!selectedHour.equals("")) {
                initExaminationHour();
                getExistedBookingList();
                inputExaminationHour.setText(selectedHour);
                inputExaminationHour.setEnabled(true);
                inputExaminationHour.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
            }
        }
    }

    private void initSpecialist() {
        inputExaminationHour.setText("");
        if (specialist.getId() != null) {
            spinnerSpecialist.setText(specialist.getName());
        }
        // Get All Specialist
        specialistList = new ArrayList<>();
        List<String> specialistIds = healthFacility.getSpecialistIds();
        if (specialistIds != null && specialistIds.size() > 0) {
            for (String id : specialistIds) {
                Task<QuerySnapshot> specialistTask = specialistDB.getById(id);
                specialistTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                            Specialist specialist = documentSnapshot.toObject(Specialist.class);
                            specialistList.add(specialist);
                        }
                    }
                    adapterSpecialist = new CommonListViewAdapter<>(specialistList, getContext());
                });
            }
        }
    }

    private void initDoctor() {
        if (doctor.getId() != null || specialist.getId() != null) {
            spinnerDoctor.setEnabled(true);
            spinnerDoctor.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
            spinnerDoctor.setText(doctor.getName());
        }
        if (specialist.getId() != null) {
            doctorList = new ArrayList<>();
            Task<QuerySnapshot> specialistTask = userDB.getDoctorByHealthFacilityAndSpecialist(healthFacility.getId(), specialist.getId());
            specialistTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        User doctor = documentSnapshot.toObject(User.class);
                        doctorList.add(doctor);
                    }
                }
                adapterDoctor = new CommonListViewAdapter<>(doctorList, getContext());
            });
        } else {
            // Get All Doctor
            doctorList = new ArrayList<>();
            Task<QuerySnapshot> specialistTask = userDB.getDoctorByHealthFacility(healthFacility.getId());
            specialistTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        User doctor = documentSnapshot.toObject(User.class);
                        doctorList.add(doctor);
                    }
                }
                adapterDoctor = new CommonListViewAdapter<>(doctorList, getContext());
            });
        }
    }

    private void initService() {
        if (serviceIdList.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < serviceIdList.size(); i++) {
                String serviceId = serviceIdList.get(i);
                Service selectedService = serviceList.stream().filter(x -> x.getId().equals(serviceId)).findFirst().orElse(null);
                if (selectedService != null) {
                    stringBuilder.append(selectedService.getName());
                    if (i != serviceIndexList.size() - 1) {
                        stringBuilder.append("; ");
                    }
                }
            }
            spinnerService.setText(stringBuilder.toString());
        }
        // Get All Service
        serviceList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        List<String> serviceIds = healthFacility.getServiceIds();
        if (serviceIds != null && serviceIds.size() > 0) {
//            AtomicInteger index = new AtomicInteger(0);
            for (String id : serviceIds) {
                Task<QuerySnapshot> serviceTask = serviceDB.getById(id);
                serviceTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                            Service service = documentSnapshot.toObject(Service.class);
                            if (specialist.getId() != null) {
                                if (service != null && service.getSpecialistIds().contains(specialist.getId())) {
                                    serviceList.add(service);
                                    serviceNameList.add(service.getName());
                                }
                            }
                        }
                    }
                    adapterService = new CommonListViewAdapter<>(serviceList, getContext());
                    isSelectedServiceList = new boolean[serviceList.size()];
                });
            }
        }
    }

    private void initExaminationHour() {
        List<Time> intervals = new ArrayList<>();
        Time startTime = new java.sql.Time(7, 0, 0);
        Time endTime = new java.sql.Time(18, 0, 0);

        intervals.add(startTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        while (cal.getTime().before(endTime)) {
            cal.add(Calendar.MINUTE, 15);
            intervals.add(new Time(cal.getTimeInMillis()));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (Time time : intervals) {
            examinationHourList.add(sdf.format(time));
        }
    }

    private void getExistedBookingList() {
        existedBookingList = new ArrayList<>();
        Task<QuerySnapshot> bookingTask = bookingDB.search(doctor.getId(), healthFacility.getId(), specialist.getId(), inputExaminationDate.getText().toString());
        bookingTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Booking booking = documentSnapshot.toObject(Booking.class);
                    existedBookingList.add(booking);
                }
            }
        });
    }

    private void initListener() {
        this.spinnerSpecialist.setOnClickListener(this);
        this.spinnerService.setOnClickListener(this);
        this.spinnerDoctor.setOnClickListener(this);
        this.inputExaminationDate.setOnClickListener(this);
        this.inputExaminationHour.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);

        this.inputExaminationDateLayout.setEndIconOnClickListener(view -> {
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
                            date = "0" + d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/" + (m + 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                    }
                    inputExaminationDate.setText(date);
                    initExaminationHour();
                    getExistedBookingList();
                    inputExaminationHour.setText("");
                    inputExaminationHour.setEnabled(true);
                    inputExaminationHour.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
                }
            }, y, m, d);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    @Override
    public void onClick(View view) {
        if (view == spinnerSpecialist) {
            TextView textView = dialog1.findViewById(R.id.title);
            ListView listView = dialog1.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterSpecialist);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterSpecialist.getItem(i).getId(), specialist.getId())) {
                        specialist = adapterSpecialist.getItem(i);
                        spinnerSpecialist.setText(adapterSpecialist.getItem(i).getName());
                        spinnerDoctor.setEnabled(true);
                        spinnerDoctor.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
                        spinnerDoctor.setText("");
                        doctor = new User();
                        serviceIdList.clear();
                        serviceNameList.clear();
                        selectedServiceList.clear();
                        serviceIndexList.clear();
                        spinnerService.setText("");
                        initDoctor();
                        initService();
                        dialog1.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Specialist");
            }
            dialog1.show();
        } else if (view == spinnerDoctor) {
            TextView textView = dialog1.findViewById(R.id.title);
            ListView listView = dialog1.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterDoctor);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterDoctor.getItem(i).getId(), doctor.getId())) {
                        doctor = adapterDoctor.getItem(i);
                        spinnerDoctor.setText(adapterDoctor.getItem(i).getName());
                        dialog1.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Doctor");
            }
            dialog1.show();
        } else if (view == spinnerService) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose Service");
            builder.setCancelable(false);

            String[] tempList = new String[serviceNameList.size()];
            builder.setMultiChoiceItems(serviceNameList.toArray(tempList), isSelectedServiceList, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b) {
                        serviceIndexList.add(i);
                    } else {
                        serviceIndexList.removeIf(integer -> integer == i);
                    }
                }
            }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int index = 0; index < serviceIndexList.size(); index++) {
                        Service selectedService = serviceList.get(serviceIndexList.get(index));
                        stringBuilder.append(selectedService.getName());
                        serviceIdList.add(selectedService.getId());
                        selectedServiceList.add(selectedService);
                        if (index != serviceIndexList.size() - 1) {
                            stringBuilder.append("; ");
                        }
                        spinnerService.setText(stringBuilder.toString());
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int index = 0; index < isSelectedServiceList.length; index++) {
                        isSelectedServiceList[index] = false;
                        serviceIdList.clear();
                        selectedServiceList.clear();
                        serviceIndexList.clear();
                        spinnerService.setText("");
                    }
                }
            });
            builder.show();
        } else if (view == inputExaminationDate) {
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
                            date = "0" + d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/" + (m + 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                    }
                    inputExaminationDate.setText(date);
                    initExaminationHour();
                    getExistedBookingList();
                    inputExaminationHour.setText("");
                    inputExaminationHour.setEnabled(true);
                    inputExaminationHour.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
                }
            }, y, m, d);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        } else if (view == inputExaminationHour) {
            CommonGridViewAdapter commonGridViewAdapter = new CommonGridViewAdapter(getContext(), inputExaminationDate.getText().toString(), examinationHourList, existedBookingList, healthFacility.getId(), doctor.getId(), specialist.getId(), doctorList);
            TextView textView = dialog2.findViewById(R.id.title);
            GridView gridView = dialog2.findViewById(R.id.gridView);
            if (gridView != null) {
                gridView.setAdapter(commonGridViewAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (view.isEnabled()) {
                            inputExaminationHour.setText(commonGridViewAdapter.getItem(i));
                            selectedHour = commonGridViewAdapter.getItem(i);
                            dialog2.dismiss();
                        }
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Examination Hour");
            }
            dialog2.show();
        } else if (view == btnNext) {
            if (inputFullName.getText().toString().equals("")) {
                FancyToast.makeText(getContext(), "FullName is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (spinnerSpecialist.getText().toString().equals("")) {
                FancyToast.makeText(getContext(), "Specialist is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (inputExaminationDate.getText().toString().equals("")) {
                FancyToast.makeText(getContext(), "Examination date is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (!inputExaminationDate.getText().toString().matches("\\d{2}/\\d{2}/\\d{4}")) {
                FancyToast.makeText(getContext(), "Examination date is invalid(dd/mm/yyyy)", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (inputExaminationHour.getText().toString().equals("")) {
                FancyToast.makeText(getContext(), "Examination hour is required", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else {
                Booking booking = new Booking();
                booking.setDoctorId(doctor.getId());
                booking.setPatientId(firebaseUser.getUid());
                booking.setPatientName(firebaseUser.getDisplayName());
                booking.setExaminationDate(inputExaminationDate.getText().toString());
                booking.setExaminationHour(inputExaminationHour.getText().toString());
                booking.setHealthFacilityId(healthFacility.getId());
                booking.setServiceIdList(serviceIdList);
                booking.setSpecialistId(specialist.getId());
                booking.setSymptom(inputSymptom.getText().toString());
                fragmentDataListener.onDataPass(booking);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentDataListener = (FragmentDataListener) context;
    }
}