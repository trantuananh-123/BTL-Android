package tran.tuananh.btl.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import tran.tuananh.btl.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnLogOut;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}