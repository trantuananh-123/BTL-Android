package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tran.tuananh.btl.R;

public class HealthFacilityManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBackArrow;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_facility_management);

        initView();
        initListener();
    }

    private void initView() {
        btnBackArrow = findViewById(R.id.btnBackArrow);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void initListener() {
        this.btnBackArrow.setOnClickListener(this);
        this.btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBackArrow) {
            finish();
        } else if (view == btnAdd) {
            startActivity(new Intent(this, AddHealthFacilityActivity.class));
        }
    }
}