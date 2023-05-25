package tran.tuananh.btl.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.LinkedHashMap;
import java.util.Map;

import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.R;
import tran.tuananh.btl.Util.FnCommon;

public class AddSpecialistActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private View progressBarBackground;

    private ImageButton btnBackArrow;
    private EditText name, code;
    private MaterialButton btnSave;
    private FirebaseFirestore firebaseFirestore;
    private SpecialistDB specialistDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_specialist);
        firebaseFirestore = FirebaseFirestore.getInstance();
        specialistDB = new SpecialistDB(this, firebaseFirestore);

        initView();
        initListener();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initListener() {
        this.btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            if (name.getText().toString().equals("")) {
                FancyToast.makeText(this, "Name is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (code.getText().toString().equals("")) {
                FancyToast.makeText(this, "Code is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBarBackground.setVisibility(View.VISIBLE);
                Map<String, Object> map = new LinkedHashMap<>();
                String id = FnCommon.generateUId();
                map.put("id", id);
                map.put("name", name.getText().toString());
                map.put("code", code.getText().toString());
                firebaseFirestore.collection("specialist").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        progressBarBackground.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FancyToast.makeText(AddSpecialistActivity.this, "Add specialist successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            finish();
                        } else {
                            FancyToast.makeText(AddSpecialistActivity.this, "Add specialist failed!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        } else if (view == btnBackArrow) {
            finish();
        }
    }
}