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
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.tuananh.btl.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private TextInputLayout inputEmailLayout, inputPasswordLayout;
    private EditText inputEmail, inputPassword;
    private TextView txtRegister;
    private ProgressBar progressBar;
    private MaterialButton btnLogin;
    private View progressBarBackground;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void initView() {
        inputPasswordLayout = findViewById(R.id.inputPasswordLayout);
        inputPassword = inputPasswordLayout.getEditText();
        inputEmailLayout = findViewById(R.id.inputEmailLayout);
        inputEmail = inputEmailLayout.getEditText();
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        progressBarBackground = findViewById(R.id.progressBarBackground);
    }

    private void initListener() {
        this.btnLogin.setOnClickListener(this);
        this.txtRegister.setOnClickListener(this);

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
                String finalPassword = password;
                String finalEmail = email;
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    progressBarBackground.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        } else if (view == txtRegister) {
            startActivity(new Intent(this, RegisterActivity.class));
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