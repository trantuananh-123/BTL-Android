package tran.tuananh.btl.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tran.tuananh.btl.Fragment.PersonalFragment.FragmentMainInfo;
import tran.tuananh.btl.Fragment.PersonalFragment.FragmentOtherInfo;

public class PersonalInfoViewPagerAdapter extends FragmentPagerAdapter {


    public PersonalInfoViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return new FragmentMainInfo();
            case 1:
                return new FragmentOtherInfo();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            default:
                return "Main";
            case 1:
                return "Other";
        }
    }
}
