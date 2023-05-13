package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.R;

public class CommonGridViewAdapter extends BaseAdapter {
    private List<String> hourList;
    private List<Booking> existedBookingList;
    private LayoutInflater layoutInflater;

    public CommonGridViewAdapter(Context context, List<String> hourList, List<Booking> existedBookingList) {
        layoutInflater = LayoutInflater.from(context);
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
        for (Booking booking : existedBookingList) {
            if (booking.getExaminationHour().equalsIgnoreCase(hour)) {
                setEnable(hourTxt, false);
            }
        }
        return hourTxt;
    }
}