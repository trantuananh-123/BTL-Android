package tran.tuananh.btl.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ImageButton btnBackArrow;
    private TextInputLayout inputFullNameLayout, inputPhoneLayout, inputEmailLayout, inputPasswordLayout;
    private EditText inputFullName, inputPhone, inputEmail, inputPassword;
    private ProgressBar progressBar;
    private MaterialButton btnRegister;
    private View progressBarBackground;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        initView();
        initListener();
    }

    private void initView() {
        btnBackArrow = findViewById(R.id.btnBackArrow);
        inputFullNameLayout = findViewById(R.id.inputFullNameLayout);
        inputFullName = inputFullNameLayout.getEditText();
        inputPhoneLayout = findViewById(R.id.inputPhoneLayout);
        inputPhone = inputPhoneLayout.getEditText();
        inputPasswordLayout = findViewById(R.id.inputPasswordLayout);
        inputPassword = inputPasswordLayout.getEditText();
        inputEmailLayout = findViewById(R.id.inputEmailLayout);
        inputEmail = inputEmailLayout.getEditText();
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
    }

    private void initListener() {
        this.btnRegister.setOnClickListener(this);
        this.btnBackArrow.setOnClickListener(this);

        inputFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputFullNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPhoneLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputEmailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarBackground.setVisibility(View.VISIBLE);
            String fullName = "", phone = "", password = "", email = "";
            fullName = String.valueOf(inputFullName.getText());
            phone = String.valueOf(inputPhone.getText());
            password = String.valueOf(inputPassword.getText());
            email = String.valueOf(inputEmail.getText());
            validate(fullName, phone, password, email);
            User user = new User();
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setEmail(email);
            if (inputFullNameLayout.getError() == null && inputPhoneLayout.getError() == null && inputPasswordLayout.getError() == null && inputEmailLayout.getError() == null) {
                String finalPassword = password;
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    HashMap<String, Object> hashMap = new LinkedHashMap<>();
                                    if (firebaseUser != null) {
                                        hashMap.put("id", firebaseUser.getUid());
                                        hashMap.put("name", user.getFullName());
                                        hashMap.put("email", user.getEmail());
                                        hashMap.put("password", finalPassword);

                                        firebaseFirestore.collection("user").document(firebaseUser.getUid()).set(hashMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FancyToast.makeText(RegisterActivity.this, "Register successfully !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        progressBar.setVisibility(View.GONE);
                                        progressBarBackground.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        progressBarBackground.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    progressBarBackground.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        } else if (view == btnBackArrow) {
            finish();
        }
    }

    private void validate(String fullName, String phone, String password, String email) {
        if (TextUtils.isEmpty(fullName)) {
            inputFullNameLayout.setError("FullName is required");
        } else if (fullName.matches(".*\\d+.*")) {
            inputFullNameLayout.setError("FullName must not contain numbers");
        } else {
            inputFullNameLayout.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            inputPhoneLayout.setError("Phone is required");
        } else if (!phone.matches("\\d+")) {
            inputPhoneLayout.setError("Phone must contain only number");
        } else if (phone.length() < 10 || phone.length() > 11) {
            inputPhoneLayout.setError("Phone must contain 10-11 number");
        } else {
            inputPhoneLayout.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputPasswordLayout.setError("Password is required");
        } else if (password.length() < 8) {
            inputPasswordLayout.setError("Password must have at least 8 characters");
        } else {
            inputPasswordLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            inputEmailLayout.setError("Email is required");
        } else if (!validateEmail(email)) {
            inputEmailLayout.setError("Invalid email");
        } else {
            inputEmailLayout.setError(null);
        }
    }

    private boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }
}