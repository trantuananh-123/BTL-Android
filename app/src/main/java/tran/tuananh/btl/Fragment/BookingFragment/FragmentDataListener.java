package tran.tuananh.btl.Fragment.BookingFragment;

import tran.tuananh.btl.Model.Booking;

public interface FragmentDataListener {

    void onDataPass(Booking booking);

    void confirmBooking(Booking booking);

    void complete(Booking booking);
}
