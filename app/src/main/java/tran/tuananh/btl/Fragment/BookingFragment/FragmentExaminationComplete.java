package tran.tuananh.btl.Fragment.BookingFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tran.tuananh.btl.Activity.HomeActivity;
import tran.tuananh.btl.Alarm.AlarmReceiver;
import tran.tuananh.btl.Database.HealthFacilityDB;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Database.UserDB;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.Model.Service;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class FragmentExaminationComplete extends Fragment implements View.OnClickListener {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");
    private Booking booking;
    private FragmentDataListener fragmentDataListener;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private MaterialButton btnReturn, btnPrintPDF;
    private UserDB userDB;
    private HealthFacilityDB healthFacilityDB;
    private SpecialistDB specialistDB;
    private ServiceDB serviceDB;
    private FirebaseFirestore firebaseFirestore;

    private User patient = new User(), doctor = new User();
    private HealthFacility healthFacility = new HealthFacility();
    private Specialist specialist = new Specialist();
    private List<Service> serviceList = new ArrayList<>();
    private String examinationDate, examinationHour;
    private Double total = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        userDB = new UserDB(getContext(), firebaseFirestore);
        healthFacilityDB = new HealthFacilityDB(getContext(), firebaseFirestore);
        specialistDB = new SpecialistDB(getContext(), firebaseFirestore);
        serviceDB = new ServiceDB(getContext(), firebaseFirestore);
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
            examinationDate = booking.getExaminationDate();
            examinationHour = booking.getExaminationHour();
            if (booking.getPatientId() != null) {
                Task<DocumentSnapshot> taskDocumentSnapshot = userDB.getById(booking.getPatientId());
                taskDocumentSnapshot.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        patient = documentSnapshot.toObject(User.class);
                    }
                });
            }
            if (booking.getDoctorId() != null) {
                Task<DocumentSnapshot> taskDocumentSnapshot = userDB.getById(booking.getDoctorId());
                taskDocumentSnapshot.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        doctor = documentSnapshot.toObject(User.class);
                    }
                });
            }
            if (booking.getHealthFacilityId() != null) {
                Task<DocumentSnapshot> taskDocumentSnapshot = healthFacilityDB.getById2(booking.getHealthFacilityId());
                taskDocumentSnapshot.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        healthFacility = documentSnapshot.toObject(HealthFacility.class);
                    }
                });
            }
            if (booking.getSpecialistId() != null) {
                Task<DocumentSnapshot> taskDocumentSnapshot = specialistDB.getById2(booking.getSpecialistId());
                taskDocumentSnapshot.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        specialist = documentSnapshot.toObject(Specialist.class);
                    }
                });
            }
            if (booking.getServiceIdList() != null && booking.getServiceIdList().size() > 0) {
                for (String i : booking.getServiceIdList()) {
                    Task<DocumentSnapshot> taskDocumentSnapshot = serviceDB.getById2(i);
                    taskDocumentSnapshot.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Service service = documentSnapshot.toObject(Service.class);
                            total += service.getPrice();
                            serviceList.add(service);
                        }
                    });
                }
            }
        }
        return inflater.inflate(R.layout.fragment_examination_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        String bookingDatePpattern = "dd/MM/yyyy HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(bookingDatePpattern);
        try {
            Date bookingDate = sdf.parse(booking.getExaminationDate() + " " + booking.getExaminationHour());

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(bookingDate.getTime());
//            calendar.setTimeInMillis(new Date().getTime());
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(booking);
            intent.putExtra("booking", json);
            alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 900000, pendingIntent);
        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.btnReturn.setOnClickListener(this);
        this.btnPrintPDF.setOnClickListener(this);
    }

    private void initView(View view) {
        btnReturn = view.findViewById(R.id.btnReturn);
        btnPrintPDF = view.findViewById(R.id.btnPrintPDF);
    }

    @Override
    public void onClick(View view) {
        if (view == btnReturn) {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
            fragmentDataListener.complete(booking);
        } else if (view == btnPrintPDF) {
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            paint.setTextSize(16);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setColor(Color.BLACK);
            float textWidth = paint.measureText("HEALTH EXAMINATION APPOINTMENT");
            float titleXPos = (canvas.getWidth() - textWidth) / 2;

            canvas.drawText("HEALTH EXAMINATION APPOINTMENT", titleXPos, 50, paint);

            paint.setTextSize(14);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Appointment ID: " + booking.getId(), 50, 100, paint);
            canvas.drawText("Patient name: " + patient.getName(), 50, 120, paint);
            canvas.drawText("HealthFacility name: " + healthFacility.getName(), 50, 140, paint);
            canvas.drawText("HealthFacility address: " + healthFacility.getAddress(), 50, 160, paint);
            canvas.drawText("Specialist: " + specialist.getName(), 50, 180, paint);
            if (doctor.getId() != null) {
                canvas.drawText("Doctor name: " + doctor.getName(), 50, 200, paint);
            }

            Paint linePaint = new Paint();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
            linePaint.setStrokeWidth(2);
            canvas.drawLine(50, doctor.getId() != null ? 220 : 200, 545, doctor.getId() != null ? 220 : 200, linePaint);

            canvas.drawText("Service: ", 50, doctor.getId() != null ? 240 : 220, paint);
            float serviceYPos = doctor.getId() != null ? 270 : 250;
            for (Service service : serviceList) {
                canvas.drawText(service.getName(), 50, serviceYPos, paint);
                canvas.drawText(String.valueOf(NUMBER_FORMAT.format(service.getPrice())) + "đ", 490, serviceYPos, paint);
                serviceYPos += 40;
            }
            canvas.drawLine(50, serviceYPos - 20, 545, serviceYPos - 20, linePaint);
            canvas.drawText("Total: ", 430, serviceYPos, paint);
            canvas.drawText(String.valueOf(NUMBER_FORMAT.format(total)) + "đ", 490, serviceYPos, paint);

            pdfDocument.finishPage(page);

            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String outputDate = "";
            try {
                date = inputFormat.parse(examinationDate);
                outputDate = outputFormat.format(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), patient.getName() + "_" + outputDate + ".pdf");

            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                fos.close();
                MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath()}, null, null);
                FancyToast.makeText(getContext(), "Print successfully. Please go to downloads folder to see file", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                pdfDocument.close();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentDataListener = (FragmentDataListener) context;
    }
}
