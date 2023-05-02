package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import tran.tuananh.btl.Adapter.PersonalInfoViewPagerAdapter;
import tran.tuananh.btl.R;

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private PersonalInfoViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btnBackArrow;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void initView() {
        btnBackArrow = findViewById(R.id.btnBackArrow);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPagerAdapter = new PersonalInfoViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initListener() {
        this.btnBackArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBackArrow) {
            finish();
        }
    }
}