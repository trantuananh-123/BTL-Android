package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.tuananh.btl.Model.User;
import tran.tuananh.btl.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private TextInputLayout inputEmailLayout, inputPasswordLayout;
    private EditText inputEmail, inputPassword;

    private TextView txtRegister, txtForgotAccount;
    private ProgressBar progressBar;
    private MaterialButton btnLogin;
    private View progressBarBackground;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        initView();
        initListener();
    }

    private void initView() {
        inputPasswordLayout = findViewById(R.id.inputPasswordLayout);
        inputPassword = inputPasswordLayout.getEditText();
        inputEmailLayout = findViewById(R.id.inputEmailLayout);
        inputEmail = inputEmailLayout.getEditText();
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotAccount = findViewById(R.id.txtForgotAccount);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
    }

    private void initListener() {
        this.btnLogin.setOnClickListener(this);
        this.txtRegister.setOnClickListener(this);
        this.txtForgotAccount.setOnClickListener(this);

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
        if (view == btnLogin) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarBackground.setVisibility(View.VISIBLE);

            String password = "", email = "";
            password = String.valueOf(inputPassword.getText());
            email = String.valueOf(inputEmail.getText());
            validate(password, email);

            if (inputPasswordLayout.getError() == null && inputEmailLayout.getError() == null) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                firebaseFirestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task1.getResult();
                                                User user = documentSnapshot.toObject(User.class);
                                                if (user != null) {
                                                    if (user.getRoleType() == 2) {
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                        finish();
                                                    } else if (user.getRoleType() == 1) {
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                        finish();
                                                    } else if (user.getRoleType() == 0) {
                                                        startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
                                                        finish();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                FancyToast.makeText(LoginActivity.this, "Login failed.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                progressBar.setVisibility(View.GONE);
                                progressBarBackground.setVisibility(View.GONE);
                            }
                        });
            } else {
                progressBar.setVisibility(View.GONE);
                progressBarBackground.setVisibility(View.GONE);
            }
        } else if (view == txtRegister) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (view == txtForgotAccount) {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }

    private void validate(String password, String email) {
        if (TextUtils.isEmpty(password)) {
            inputPasswordLayout.setError("Password is required");
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