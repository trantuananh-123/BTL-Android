package tran.tuananh.btl.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import tran.tuananh.btl.Fragment.MainFragment.FragmentAppointment;
import tran.tuananh.btl.Fragment.MainFragment.FragmentHome;
import tran.tuananh.btl.Fragment.MainFragment.FragmentManagement;
import tran.tuananh.btl.Fragment.MainFragment.FragmentNotification;
import tran.tuananh.btl.Fragment.MainFragment.FragmentPersonal;

public class AdminMainViewPagerAdapter extends FragmentStatePagerAdapter {

    int totalPager = 4;

    public AdminMainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return new FragmentHome();
            case 1:
                return new FragmentManagement();
            case 2:
                return new FragmentNotification();
            case 3:
                return new FragmentPersonal();
        }
    }

    @Override
    public int getCount() {
        return this.totalPager;
    }
}
