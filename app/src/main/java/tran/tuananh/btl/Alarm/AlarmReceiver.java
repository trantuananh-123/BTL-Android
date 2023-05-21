package tran.tuananh.btl.Alarm;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.R;
import tran.tuananh.btl.Util.FnCommon;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String json = intent.getStringExtra("booking");
        ObjectMapper objectMapper = new ObjectMapper();
        Booking booking = new Booking();
        try {
            booking = objectMapper.readValue(json, Booking.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Booking finalBooking = booking;
        firebaseFirestore.collection("healthFacility").document(booking.getHealthFacilityId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        HealthFacility healthFacility = documentSnapshot.toObject(HealthFacility.class);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("bookingChannel", "Channel booking", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("THONG BAO LICH KHAM");
                            notificationManager.createNotificationChannel(channel);
                        }

                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        String message = "Ban co lich kham vao ngay " + finalBooking.getExaminationDate() + " luc " + finalBooking.getExaminationHour() + " tai benh vien " + healthFacility.getName();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bookingChannel")
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Nhac nho lich kham")
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("Ban co lich kham vao ngay " + finalBooking.getExaminationDate() + " luc " + finalBooking.getExaminationHour() + " tai benh vien " + healthFacility.getName()))
                                .setCategory(NotificationCompat.CATEGORY_EVENT)
                                .setSound(alarmSound)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        HashMap<String, Object> map = new LinkedHashMap<>();
                        String id = FnCommon.generateUId();
                        map.put("id", id);
                        map.put("examinationDate", finalBooking.getExaminationDate());
                        map.put("examinationHour", finalBooking.getExaminationHour());
                        map.put("doctorId", finalBooking.getDoctorId());
                        map.put("patientId", finalBooking.getPatientId());
                        map.put("healthFacilityId", finalBooking.getHealthFacilityId());
                        map.put("message", message);

                        firebaseFirestore.collection("notification").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        notificationManagerCompat.notify(getNotificationId(), builder.build());
                    }
                });
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}
