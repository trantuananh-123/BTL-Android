package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import tran.tuananh.btl.Adapter.MainViewPagerAdapter;
import tran.tuananh.btl.R;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        initListener();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainViewPagerAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            int menuPosition = intent.getIntExtra("menuPosition", 0);
            viewPager.setCurrentItem(menuPosition);
            switch (menuPosition) {
                case 0:
                default:
                    bottomNavigationView.getMenu().findItem(R.id.homeMenu).setChecked(true);
                    break;
                case 1:
                    bottomNavigationView.getMenu().findItem(R.id.appointmentMenu).setChecked(true);
                    break;
                case 2:
                    bottomNavigationView.getMenu().findItem(R.id.notificationMenu).setChecked(true);
                    break;
                case 3:
                    bottomNavigationView.getMenu().findItem(R.id.personalMenu).setChecked(true);
                    break;
            }
        }
    }

    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.homeMenu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.appointmentMenu).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.notificationMenu).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.personalMenu).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeMenu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.appointmentMenu:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.notificationMenu:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.personalMenu:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}