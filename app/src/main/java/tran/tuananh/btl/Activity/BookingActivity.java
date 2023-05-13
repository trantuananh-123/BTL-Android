package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.shuhart.stepview.StepView;

import tran.tuananh.btl.Fragment.BookingFragment.FragmentDataListener;
import tran.tuananh.btl.Fragment.BookingFragment.FragmentExaminationComplete;
import tran.tuananh.btl.Fragment.BookingFragment.FragmentExaminationInfo;
import tran.tuananh.btl.Fragment.BookingFragment.FragmentExaminationSummary;
import tran.tuananh.btl.Model.Booking;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.R;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener, FragmentDataListener {

    private StepView stepView;
    private HealthFacility healthFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initView();
        initData();
        initListener();

        Intent intent = getIntent();
        healthFacility = (HealthFacility) intent.getSerializableExtra("healthFacility");

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("healthFacility", healthFacility);
            FragmentExaminationInfo fragmentExaminationInfo = new FragmentExaminationInfo();
            fragmentExaminationInfo.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, fragmentExaminationInfo)
                    .addToBackStack("FragmentExaminationInfo")
                    .commit();
        }
    }

    private void initView() {
        stepView = findViewById(R.id.stepView);
    }

    private void initData() {
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDataPass(Booking booking) {
        stepView.go(stepView.getCurrentStep() + 1, true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("booking", booking);
        FragmentExaminationSummary fragmentExaminationSummary = new FragmentExaminationSummary();
        fragmentExaminationSummary.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentExaminationSummary)
                .addToBackStack("FragmentExaminationSummary")
                .commit();
    }

    @Override
    public void confirmBooking(Booking booking) {
        stepView.go(stepView.getCurrentStep() + 1, true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("booking", booking);
        FragmentExaminationComplete fragmentExaminationComplete = new FragmentExaminationComplete();
        fragmentExaminationComplete.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentExaminationComplete)
                .addToBackStack("FragmentExaminationComplete")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stepView.go(stepView.getCurrentStep() - 1, true);
        stepView.done(false);
    }
}