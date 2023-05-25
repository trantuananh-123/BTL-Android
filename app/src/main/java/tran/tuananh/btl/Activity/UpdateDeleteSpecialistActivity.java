package tran.tuananh.btl.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.R;

public class UpdateDeleteSpecialistActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private View progressBarBackground;

    private ImageButton btnBackArrow;
    private EditText name, code;
    private MaterialButton btnSave, btnDelete;
    private FirebaseFirestore firebaseFirestore;
    private SpecialistDB specialistDB;
    private Specialist specialist = new Specialist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_specialist);
        firebaseFirestore = FirebaseFirestore.getInstance();
        specialistDB = new SpecialistDB(this, firebaseFirestore);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void initData() {
        Intent intent = getIntent();
        specialist = (Specialist) intent.getSerializableExtra("specialist");
        if (specialist != null) {
            name.setText(specialist.getName());
            code.setText(specialist.getCode());
        }
    }

    private void initListener() {
        this.btnSave.setOnClickListener(this);
        this.btnDelete.setOnClickListener(this);
        this.btnBackArrow.setOnClickListener(this);
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
                map.put("id", specialist.getId());
                map.put("name", name.getText().toString());
                map.put("code", code.getText().toString());
                firebaseFirestore.collection("specialist").document(specialist.getId()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        progressBarBackground.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FancyToast.makeText(UpdateDeleteSpecialistActivity.this, "Update specialist successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            finish();
                        } else {
                            FancyToast.makeText(UpdateDeleteSpecialistActivity.this, "Update specialist failed!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        } else if (view == btnDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Specialist");
            builder.setMessage("Are you sure want to delete " + specialist.getName() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseFirestore.collection("specialist").document(specialist.getId()).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FancyToast.makeText(UpdateDeleteSpecialistActivity.this, "Delete specialist success!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                    finish();
                                }
                            });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } else if (view == btnBackArrow) {
            finish();
        }
    }
}