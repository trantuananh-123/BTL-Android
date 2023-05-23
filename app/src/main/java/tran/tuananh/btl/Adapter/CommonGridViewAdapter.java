package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import tran.tuananh.btl.Database.BookingDB;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class CommonGridViewAdapter extends BaseAdapter {
    private List<User> doctorList;
    private String healthFacilityId;
    private String doctorId;
    private String specialistId;
    private String examinationDate;
    private List<String> hourList;
    private List<Booking> existedBookingList;
    private LayoutInflater layoutInflater;
    private BookingDB bookingDB;

    public CommonGridViewAdapter(Context context, String examinationDate, List<String> hourList, List<Booking> existedBookingList, String healthFacilityId, String doctorId, String specialistId, List<User> doctorList) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        layoutInflater = LayoutInflater.from(context);
        this.examinationDate = examinationDate;
        this.hourList = hourList;
        this.existedBookingList = existedBookingList;
        this.healthFacilityId = healthFacilityId;
        this.doctorId = doctorId;
        this.specialistId = specialistId;
        this.doctorList = doctorList;
        this.bookingDB = new BookingDB(context, firebaseFirestore);
    }

    @Override
    public int getCount() {
        return hourList == null ? 0 : hourList.size();
    }

    @Override
    public String getItem(int position) {
        return hourList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setEnable(TextView textView, boolean isEnable) {
        textView.setEnabled(isEnable);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String hour = hourList.get(position);
        TextView hourTxt;
        if (convertView == null) {
            convertView =
                    layoutInflater.inflate(R.layout.hour_item_spinner, parent, false);
            hourTxt = (TextView) convertView.findViewById(R.id.hourValue);
        } else {
            hourTxt = (TextView) convertView;
        }
        hourTxt.setText(hour);

        Calendar now = new GregorianCalendar();
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);

        String[] parts = hour.split(":");

        int myHour = Integer.parseInt(parts[0]);
        int myMinute = Integer.parseInt(parts[1]);

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate myDate = LocalDate.parse(examinationDate, formatter);
            LocalDate currentDate = LocalDate.now();
            if (myDate.isBefore(currentDate) || myDate.isEqual(currentDate)) {
                if (60 * nowHour + nowMin > 60 * myHour + myMinute) {
                    setEnable(hourTxt, false);
                }
            }
        }

        for (Booking booking : existedBookingList) {
            if (booking.getExaminationHour().equalsIgnoreCase(hour)) {
                setEnable(hourTxt, false);
            }
        }
        if (doctorId == null) {
            AggregateQuery aggregateQuery = bookingDB.isExist(null, healthFacilityId, specialistId, examinationDate, hour);
            aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        AggregateQuerySnapshot snapshot = task.getResult();
                        if (snapshot.getCount() >= doctorList.size()) {
                            setEnable(hourTxt, false);
                        } else {
                            setEnable(hourTxt, true);
                        }
                    }
                }
            });
        }
        return hourTxt;
    }
}