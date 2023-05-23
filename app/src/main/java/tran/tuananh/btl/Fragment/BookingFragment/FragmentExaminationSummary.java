package tran.tuananh.btl.Fragment.BookingFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import tran.tuananh.btl.Database.BookingDB;
import tran.tuananh.btl.Database.HealthFacilityDB;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Database.UserDB;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;
import tran.tuananh.btl.Util.FnCommon;

public class FragmentExaminationSummary extends Fragment implements View.OnClickListener {
    private FragmentDataListener fragmentDataListener;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");
    private ProgressBar progressBar;
    private View progressBarBackground;
    private TextView healthFacilityName, patientName, healthFacilityAddress, serviceList, examinationTime, doctorName, symptom, totalMoney;
    private MaterialButton btnSave;
    private Booking booking;
    private HealthFacility healthFacility;
    private List<String> serviceIdList;
    private User doctor;
    private List<Booking> bookingList = new ArrayList<>();
    private HealthFacilityDB healthFacilityDB;
    private ServiceDB serviceDB;
    private UserDB userDB;
    private BookingDB bookingDB;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
            serviceIdList = booking.getServiceIdList();
        }
        return inflater.inflate(R.layout.fragment_examination_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        healthFacilityDB = new HealthFacilityDB(getContext(), firebaseFirestore);
        serviceDB = new ServiceDB(getContext(), firebaseFirestore);
        userDB = new UserDB(getContext(), firebaseFirestore);
        bookingDB = new BookingDB(getContext(), firebaseFirestore);

        healthFacilityName = view.findViewById(R.id.healthFacilityName);
        patientName = view.findViewById(R.id.patientName);
        healthFacilityAddress = view.findViewById(R.id.healthFacilityAddress);
        serviceList = view.findViewById(R.id.service);
        examinationTime = view.findViewById(R.id.examinationTime);
        doctorName = view.findViewById(R.id.doctorName);
        symptom = view.findViewById(R.id.symptom);
        totalMoney = view.findViewById(R.id.totalMoney);
        btnSave = view.findViewById(R.id.btnSave);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);

        if (getArguments() != null) {
            patientName.append(firebaseUser != null ? firebaseUser.getDisplayName() : "");
            initHealthFacility();
            initService();
            examinationTime.append(booking.getExaminationDate() + " " + booking.getExaminationHour());
            initDoctor();
            symptom.append("\n" + booking.getSymptom());
        }

        Task<QuerySnapshot> querySnapshotTask = bookingDB.getAll();
        querySnapshotTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        bookingList.add(documentSnapshot.toObject(Booking.class));
                    }
                }
            }
        });

        this.btnSave.setOnClickListener(this);
    }

    private void initHealthFacility() {
        Task<QuerySnapshot> querySnapshotTask = healthFacilityDB.getById(booking.getHealthFacilityId());
        querySnapshotTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    healthFacility = documentSnapshot.toObject(HealthFacility.class);
                    if (healthFacility != null) {
                        healthFacilityName.append(healthFacility.getName());
                        healthFacilityAddress.append(healthFacility.getAddress());
                    }
                }
            }
        });
    }

    private void initService() {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        for (String id : serviceIdList) {
            Task<QuerySnapshot> querySnapshotTask = serviceDB.getById(id);
            querySnapshotTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Service service = documentSnapshot.toObject(Service.class);
                        if (service != null) {
                            serviceList.append("\n " + "+ " + service.getName() + " - " + NUMBER_FORMAT.format(service.getPrice()) + "đ");
                            total.updateAndGet(v -> v + service.getPrice());
                            totalMoney.setText("Total money: " + NUMBER_FORMAT.format(total.get()));
                        }
                    }
                }
            });
        }
    }

    private void initDoctor() {
        if (booking.getDoctorId() != null && !booking.getDoctorId().equals("")) {
            Task<QuerySnapshot> querySnapshotTask = userDB.getDoctorById(booking.getDoctorId());
            querySnapshotTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        doctor = documentSnapshot.toObject(User.class);
                        if (doctor != null) {
                            doctorName.append(doctor.getName());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            AggregateQuery aggregateQuery = bookingDB.isExist(booking.getDoctorId(), booking.getHealthFacilityId(), booking.getSpecialistId(), booking.getExaminationDate(), booking.getExaminationHour());
            aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        AggregateQuerySnapshot snapshot = task.getResult();
//                        if (snapshot.getCount() > 0) {
//                            FancyToast.makeText(getContext(), "Examination time is unavailable", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBarBackground.setVisibility(View.VISIBLE);
                            HashMap<String, Object> hashMap = new LinkedHashMap<>();
                            String id = FnCommon.generateUId();
                            booking.setId(id);
                            hashMap.put("id", booking.getId());
                            hashMap.put("doctorId", booking.getDoctorId());
                            hashMap.put("patientId", firebaseUser.getUid());
                            hashMap.put("examinationDate", booking.getExaminationDate());
                            hashMap.put("examinationHour", booking.getExaminationHour());
                            hashMap.put("healthFacilityId", booking.getHealthFacilityId());
                            hashMap.put("serviceIdList", booking.getServiceIdList());
                            hashMap.put("specialistId", booking.getSpecialistId());
                            hashMap.put("symptom", booking.getSymptom());
                            firebaseFirestore.collection("booking").document().set(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBarBackground.setVisibility(View.GONE);
                                    FancyToast.makeText(getContext(), "Booking successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                    fragmentDataListener.confirmBooking(booking);
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                            progressBarBackground.setVisibility(View.GONE);
                            FancyToast.makeText(getContext(), "Booking successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            fragmentDataListener.confirmBooking(booking);
//                        }
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentDataListener = (FragmentDataListener) context;
    }
}
