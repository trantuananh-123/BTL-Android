package tran.tuananh.btl.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Database.ServiceDB;
import tran.tuananh.btl.Database.SpecialistDB;
import tran.tuananh.btl.Model.Specialist;
import tran.tuananh.btl.R;
import tran.tuananh.btl.Util.FnCommon;

public class AddServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private View progressBarBackground;

    private ImageButton btnBackArrow;
    private EditText name, code, price;
    private TextView spinnerSpecialist;
    private MaterialButton btnSave;
    private BottomSheetDialog dialog;
    private FirebaseFirestore firebaseFirestore;
    private ServiceDB serviceDB;
    private SpecialistDB specialistDB;
    private List<Specialist> specialistList = new ArrayList<>();
    private List<Integer> specialistIndexList = new ArrayList<>();
    private List<String> specialistIdList = new ArrayList<>();
    private boolean[] isSelectedSpecialistList;
    private List<String> specialistNameList = new ArrayList<>();
    private CommonListViewAdapter<Specialist> adapterSpecialist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        firebaseFirestore = FirebaseFirestore.getInstance();
        serviceDB = new ServiceDB(this, firebaseFirestore);
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
        price = findViewById(R.id.price);
        btnSave = findViewById(R.id.btnSave);
        spinnerSpecialist = findViewById(R.id.spinnerSpecialist);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        initSpecialist();
    }

    private void initSpecialist() {
        // Get All Specialist
        specialistList = new ArrayList<>();
        isSelectedSpecialistList = new boolean[specialistList.size()];
        specialistIdList.clear();
        specialistIndexList.clear();
        specialistNameList.clear();
        Task<QuerySnapshot> specialistTask = specialistDB.getAll();
        specialistTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> specialistSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot specialistSnapshot : specialistSnapshotList) {
                    Specialist specialist = specialistSnapshot.toObject(Specialist.class);
                    if (specialist != null) {
                        specialistList.add(specialist);
                        specialistNameList.add(specialist.getName());
                    }
                }
            }
            adapterSpecialist = new CommonListViewAdapter<>(specialistList, this);
            isSelectedSpecialistList = new boolean[specialistList.size()];
        });
    }

    private void initListener() {
        this.btnSave.setOnClickListener(this);
        this.spinnerSpecialist.setOnClickListener(this);
        this.btnBackArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == spinnerSpecialist) {
            final int[] count = {0};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Specialist");
            builder.setCancelable(false);

            String[] tempList = new String[specialistNameList.size()];
            builder.setMultiChoiceItems(specialistNameList.toArray(tempList), isSelectedSpecialistList, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b) {
                        specialistIndexList.add(i);
                        count[0]++;
                    } else {
                        specialistIndexList.removeIf(integer -> integer == i);
                        count[0]--;
                    }
                }
            }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder stringBuilder = new StringBuilder();
                    specialistIdList = new ArrayList<>();
                    for (int index = 0; index < specialistIndexList.size(); index++) {
                        Specialist selectedSpecialist = specialistList.get(specialistIndexList.get(index));
                        stringBuilder.append(selectedSpecialist.getName());
                        if (!specialistIdList.contains(selectedSpecialist.getId())) {
                            specialistIdList.add(selectedSpecialist.getId());
                        }
                        if (index != specialistIndexList.size() - 1) {
                            stringBuilder.append("; ");
                        }
                        spinnerSpecialist.setText(stringBuilder.toString());
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    while (count[0]-- > 0) {
                        specialistIndexList.remove(specialistIndexList.size() - 1);
                    }
                    dialogInterface.dismiss();
                }
            }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int index = 0; index < isSelectedSpecialistList.length; index++) {
                        isSelectedSpecialistList[index] = false;
                        specialistIdList.clear();
                        specialistIndexList.clear();
                        spinnerSpecialist.setText("");
                    }
                }
            });
            builder.show();
        } else if (view == btnSave) {
            if (name.getText().toString().equals("")) {
                FancyToast.makeText(this, "Name is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (code.getText().toString().equals("")) {
                FancyToast.makeText(this, "Code is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (price.getText().toString().equals("")) {
                FancyToast.makeText(this, "Price is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (!price.getText().toString().matches("^[0-9]+(.|,)?[0-9]+?$")) {
                FancyToast.makeText(this, "Price is invalid!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else if (specialistIdList.size() == 0) {
                FancyToast.makeText(this, "Specialist is required!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBarBackground.setVisibility(View.VISIBLE);
                Map<String, Object> map = new LinkedHashMap<>();
                String id = FnCommon.generateUId();
                map.put("id", id);
                map.put("name", name.getText().toString());
                map.put("code", code.getText().toString());
                map.put("price", Double.parseDouble(price.getText().toString()));
                map.put("specialistIds", specialistIdList);

                firebaseFirestore.collection("service").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        progressBarBackground.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FancyToast.makeText(AddServiceActivity.this, "Add service successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            finish();
                        } else {
                            FancyToast.makeText(AddServiceActivity.this, "Add service failed!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        } else if (view == btnBackArrow) {
            finish();
        }
    }
}