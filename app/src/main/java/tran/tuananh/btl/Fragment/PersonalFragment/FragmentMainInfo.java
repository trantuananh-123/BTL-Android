package tran.tuananh.btl.Fragment.PersonalFragment;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.tuananh.btl.Activity.HomeActivity;
import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Adapter.CommonListViewAdapter;
import tran.tuananh.btl.Database.DistrictDB;
import tran.tuananh.btl.Database.ProvinceDB;
import tran.tuananh.btl.Database.WardDB;
import tran.tuananh.btl.Model.District;
import tran.tuananh.btl.Model.Province;
import tran.tuananh.btl.Model.Ward;
import tran.tuananh.btl.R;

public class FragmentMainInfo extends Fragment implements View.OnClickListener {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ImageView inputAvatar;
    private Uri avatarUri;
    private String avatarUrl;
    private TextInputLayout inputFullNameLayout, inputEmailLayout, inputBirthdayLayout, inputIdCardLayout, inputPhoneLayout;
    private EditText inputFullName, inputEmail, inputBirthday, inputIdCard, inputPhone;
    private RadioButton inputGenderMale, inputGenderFemale, inputGenderOther;
    private TextView spinnerProvince, spinnerDistrict, spinnerWard;
    private BottomSheetDialog dialog;
    private ProgressBar progressBar;
    private View progressBarBackground;
    private MaterialButton btnSave;
    private CommonListViewAdapter<Province> adapterProvince;
    private CommonListViewAdapter<District> adapterDistrict;
    private CommonListViewAdapter<Ward> adapterWard;
    private List<Province> provinceList = new ArrayList<>();
    private List<District> districtList = new ArrayList<>();
    private List<Ward> wardList = new ArrayList<>();
    private ProvinceDB provinceDB;
    private DistrictDB districtDB;
    private WardDB wardDB;
    private Province province = new Province();
    private District district = new District();
    private Ward ward = new Ward();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_main_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        provinceDB = new ProvinceDB(getContext(), firebaseFirestore);
        districtDB = new DistrictDB(getContext(), firebaseFirestore);
        wardDB = new WardDB(getContext(), firebaseFirestore);

        initView(view);
        initData(view);
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null && firebaseUser == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        inputAvatar = view.findViewById(R.id.inputAvatar);
        inputFullNameLayout = view.findViewById(R.id.inputFullNameLayout);
        inputFullName = inputFullNameLayout.getEditText();
        inputEmailLayout = view.findViewById(R.id.inputEmailLayout);
        inputEmail = inputEmailLayout.getEditText();
        inputPhoneLayout = view.findViewById(R.id.inputPhoneLayout);
        inputPhone = inputPhoneLayout.getEditText();
        inputBirthdayLayout = view.findViewById(R.id.inputBirthdayLayout);
        inputBirthday = inputBirthdayLayout.getEditText();
        inputIdCardLayout = view.findViewById(R.id.inputIdCardLayout);
        inputIdCard = inputIdCardLayout.getEditText();
        inputGenderMale = view.findViewById(R.id.inputGenderMale);
        inputGenderFemale = view.findViewById(R.id.inputGenderFemale);
        inputGenderOther = view.findViewById(R.id.inputGenderOther);
        spinnerProvince = view.findViewById(R.id.spinnerProvince);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        btnSave = view.findViewById(R.id.btnSave);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);

        spinnerDistrict.setEnabled(false);
        spinnerDistrict.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_disabled));
        spinnerWard = view.findViewById(R.id.spinnerWard);
        spinnerWard.setEnabled(false);
        spinnerWard.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_disabled));

        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData(View view) {
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        // Fill user information
        firebaseFirestore.collection("user").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        inputFullName.setText((CharSequence) documentSnapshot.get("name"));
                        inputEmail.setText((CharSequence) documentSnapshot.get("email"));
                        inputPhone.setText((CharSequence) documentSnapshot.get("phone"));
                        inputBirthday.setText(documentSnapshot.get("birthday") != null ? (CharSequence) documentSnapshot.get("birthday") : "");
                        inputIdCard.setText(documentSnapshot.get("idCard") != null ? (CharSequence) documentSnapshot.get("idCard") : "");
                        if (documentSnapshot.get("gender") != null) {
                            String gender = documentSnapshot.get("gender").toString();
                            if (gender.equalsIgnoreCase("male")) {
                                inputGenderMale.setChecked(true);
                            } else if (gender.equalsIgnoreCase("female")) {
                                inputGenderFemale.setChecked(true);
                            } else {
                                inputGenderOther.setChecked(true);
                            }
                        }
                        String avatar = (String) documentSnapshot.get("avatar");
                        if (avatar != null) {
                            Glide.with(view).load(avatar).into(inputAvatar);
                        }
                        HashMap<String, Object> provinceReference = (HashMap<String, Object>) documentSnapshot.get("province");
                        if (provinceReference != null) {
                            province.setId((Long) provinceReference.get("id"));
                            province.setCode((String) provinceReference.get("code"));
                            province.setName((String) provinceReference.get("name"));
                            spinnerProvince.setText((CharSequence) provinceReference.get("name"));
                            spinnerDistrict.setEnabled(true);
                            spinnerDistrict.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
                            initDistrict();
                        }
                        HashMap<String, Object> districtReference = (HashMap<String, Object>) documentSnapshot.get("district");
                        if (districtReference != null) {
                            district.setId((Long) districtReference.get("id"));
                            district.setCode((String) districtReference.get("code"));
                            district.setName((String) districtReference.get("name"));
                            district.setProvince(province);
                            spinnerDistrict.setText((CharSequence) districtReference.get("name"));
                            spinnerWard.setEnabled(true);
                            spinnerWard.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_spinner_enabled));
                            initWard();
                        }
                        HashMap<String, Object> wardReference = (HashMap<String, Object>) documentSnapshot.get("ward");
                        if (wardReference != null) {
                            ward.setId((Long) wardReference.get("id"));
                            ward.setCode((String) wardReference.get("code"));
                            ward.setName((String) wardReference.get("name"));
                            ward.setProvince(province);
                            ward.setDistrict(district);
                            spinnerWard.setText((CharSequence) wardReference.get("name"));
                        }
                    }
                });
        initProvince();
    }

    private void initProvince() {
        // Get All Province
        provinceList = new ArrayList<>();
        Task<QuerySnapshot> provinceTask = provinceDB.getAll();
        provinceTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                    Province province = documentSnapshot.toObject(Province.class);
                    provinceList.add(province);
                }
            }
            adapterProvince = new CommonListViewAdapter<>(provinceList, getContext());
        });
    }

    private void initDistrict() {
        // Get All District
        districtList = new ArrayList<>();
        Task<QuerySnapshot> districtTask = districtDB.getAllByProvince(province.getCode());
        districtTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> districtSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot districtSnapshot : districtSnapshotList) {
                    District district = districtSnapshot.toObject(District.class);
                    districtList.add(district);
                }
            }
            adapterDistrict = new CommonListViewAdapter<>(districtList, getContext());
        });
    }

    private void initWard() {
        // Get All Ward
        wardList = new ArrayList<>();
        Task<QuerySnapshot> wardTask = wardDB.getAllByProvinceAndDistrict(province.getCode(), district.getCode());
        wardTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> wardSnapshotList = task.getResult().getDocuments();
                for (DocumentSnapshot wardSnapshot : wardSnapshotList) {
                    Ward ward = wardSnapshot.toObject(Ward.class);
                    wardList.add(ward);
                }
            }
            adapterWard = new CommonListViewAdapter<>(wardList, getContext());
        });
    }

    private void initListener() {
        this.inputAvatar.setOnClickListener(this);
        this.spinnerProvince.setOnClickListener(this);
        this.spinnerDistrict.setOnClickListener(this);
        this.spinnerWard.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
        this.inputBirthday.setOnClickListener(this);
        this.inputBirthdayLayout.setEndIconOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputBirthday.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        });
    }

    @Override
    public void onClick(View view) {
        if (view == inputAvatar) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        } else if (view == spinnerProvince) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterProvince);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterProvince.getItem(i).getId(), province.getId())) {
                        province = adapterProvince.getItem(i);
                        spinnerProvince.setText(adapterProvince.getItem(i).getName());

                        initDistrict();
                        district = new District();
                        spinnerDistrict.setEnabled(true);
                        spinnerDistrict.setText("");
                        spinnerDistrict.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_spinner_enabled));
                        ward = new Ward();
                        spinnerWard.setEnabled(false);
                        spinnerWard.setText("");
                        spinnerWard.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_spinner_disabled));

                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Province");
            }
            dialog.show();
        } else if (view == spinnerDistrict) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterDistrict);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterDistrict.getItem(i).getId(), district.getId())) {
                        district = adapterDistrict.getItem(i);
                        spinnerDistrict.setText(adapterDistrict.getItem(i).getName());
                        initWard();
                        ward = new Ward();
                        spinnerWard.setEnabled(true);
                        spinnerWard.setText("");
                        spinnerWard.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_spinner_enabled));
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose District");
            }
            dialog.show();
        } else if (view == spinnerWard) {
            TextView textView = dialog.findViewById(R.id.title);
            ListView listView = dialog.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(adapterWard);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    if (!Objects.equals(adapterWard.getItem(i).getId(), ward.getId())) {
                        ward = adapterWard.getItem(i);
                        spinnerWard.setText(adapterWard.getItem(i).getName());
                        dialog.dismiss();
                    }
                });
            }
            if (textView != null) {
                textView.setText("Choose Ward");
            }
            dialog.show();
        } else if (view == inputBirthday) {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                String date = "";

                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if (m >= 9) {
                        if (d < 10) {
                            date = "0" + d + "/" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/" + (m - 1) + "/" + y;
                        }
                    } else {
                        if (d < 10) {
                            date = "0" + d + "/0" + (m - 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m - 1) + "/" + y;
                        }
                    }
                    inputBirthday.setText(date);
                }
            }, y, m, d);
            datePickerDialog.show();
        } else if (view == btnSave) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarBackground.setVisibility(View.VISIBLE);

            String fullName = "", email = "", birthday = "";
            fullName = String.valueOf(inputFullName.getText());
            email = String.valueOf(inputEmail.getText());
            birthday = String.valueOf(inputBirthday.getText());
            validate(fullName, email, birthday);

            if (inputFullNameLayout.getError() == null && inputEmailLayout.getError() == null && inputBirthdayLayout.getError() == null) {
                HashMap<String, Object> hashMap = new LinkedHashMap<>();
                hashMap.put("id", firebaseUser.getUid());
                hashMap.put("name", fullName);
                hashMap.put("phone", inputPhone.getText().toString());
                hashMap.put("birthday", birthday);
                hashMap.put("email", email);
                hashMap.put("gender", inputGenderMale.isChecked() ? "male" : inputGenderFemale.isChecked() ? "female" : "unknown");
                hashMap.put("idCard", inputIdCard.getText().toString());
                hashMap.put("province", province);
                hashMap.put("district", district);
                hashMap.put("ward", ward);

                StorageReference storageReference = firebaseStorage.getReference().child(System.currentTimeMillis() + "." + getFileExtension(avatarUri));
                storageReference.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                avatarUrl = uri.toString();
                                hashMap.put("avatar", avatarUrl);
                                firebaseFirestore.collection("user").document(firebaseUser.getUid()).update(hashMap)
                                        .addOnCompleteListener(task -> {
                                            progressBar.setVisibility(View.GONE);
                                            progressBarBackground.setVisibility(View.GONE);
                                            Intent intent = new Intent(getContext(), HomeActivity.class);
                                            intent.putExtra("menuPosition", 3);
                                            FancyToast.makeText(getContext(), "Update info successfully !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            progressBar.setVisibility(View.GONE);
                                            progressBarBackground.setVisibility(View.GONE);
                                            FancyToast.makeText(getContext(), "Update info failed.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                        });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }

    private void validate(String fullName, String email, String birthday) {
        if (TextUtils.isEmpty(fullName)) {
            inputFullNameLayout.setError("Password is required");
        } else {
            inputFullNameLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            inputEmailLayout.setError("Email is required");
        } else if (!validateEmail(email)) {
            inputEmailLayout.setError("Invalid email");
        } else {
            inputEmailLayout.setError(null);
        }

        if(!birthday.matches("\\d{2}/\\d{2}/\\d{4}")){
            inputBirthdayLayout.setError("Invalid date(mm/dd/yyyy)");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            avatarUri = data.getData();
            inputAvatar.setImageURI(avatarUri);
        }
    }

    private boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
