package tran.tuananh.btl.Fragment.BookingFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tran.tuananh.btl.Activity.HomeActivity;
import tran.tuananh.btl.Alarm.AlarmReceiver;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.R;

public class FragmentExaminationComplete extends Fragment implements View.OnClickListener {

    private Booking booking;
    private FragmentDataListener fragmentDataListener;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private MaterialButton btnReturn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
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
    }

    private void initView(View view) {
        btnReturn = view.findViewById(R.id.btnReturn);
    }

    @Override
    public void onClick(View view) {
        if (view == btnReturn) {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
            fragmentDataListener.complete(booking);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentDataListener = (FragmentDataListener) context;
    }
}
