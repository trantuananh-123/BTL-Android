package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.R;

public class CommonGridViewAdapter extends BaseAdapter {
    private String examinationDate;
    private List<String> hourList;
    private List<Booking> existedBookingList;
    private LayoutInflater layoutInflater;

    public CommonGridViewAdapter(Context context, String examinationDate, List<String> hourList, List<Booking> existedBookingList) {
        layoutInflater = LayoutInflater.from(context);
        this.examinationDate = examinationDate;
        this.hourList = hourList;
        this.existedBookingList = existedBookingList;
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
        return hourTxt;
    }
}