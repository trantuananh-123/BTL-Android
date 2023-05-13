package tran.tuananh.btl.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import tran.tuananh.btl.Fragment.HealthFacilityDetailFragment.FragmentDoctor;
import tran.tuananh.btl.Fragment.HealthFacilityDetailFragment.FragmentInfo;
import tran.tuananh.btl.Fragment.HealthFacilityDetailFragment.FragmentService;

public class HealthFacilityDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    int totalPager = 3;

    public HealthFacilityDetailViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return new FragmentInfo();
            case 1:
                return new FragmentService();
            case 2:
                return new FragmentDoctor();
        }
    }

    @Override
    public int getCount() {
        return this.totalPager;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            default:
                return "Introduction";
            case 1:
                return "Service";
            case 2:
                return "Doctor";
        }
    }
}
