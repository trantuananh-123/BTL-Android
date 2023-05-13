package tran.tuananh.btl.Fragment.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import tran.tuananh.btl.Activity.ChooseHealthFacilityActivity;
import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.R;

public class FragmentHome extends Fragment implements View.OnClickListener {

    private ImageButton btnBookingAppointment;
    private TextView bookAppointmentTxt;
    private ShapeableImageView avatar;
    private TextView fullName;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
        btnBookingAppointment = view.findViewById(R.id.btnBookingAppointment);
        bookAppointmentTxt = view.findViewById(R.id.bookAppointmentTxt);
        fullName = view.findViewById(R.id.fullName);
        avatar = view.findViewById(R.id.avatar);
    }

    private void initData(View view) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            fullName.setText(firebaseUser.getDisplayName());
            firebaseFirestore.collection("user").document(firebaseUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String avatar = (String) documentSnapshot.get("avatar");
                            if (avatar != null) {
                                Glide.with(view).load(avatar).into(this.avatar);
                            }
                        }
                    });
        }
    }

    private void initListener() {
        btnBookingAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBookingAppointment || view == bookAppointmentTxt) {
            Intent intent = new Intent(this.getContext(), ChooseHealthFacilityActivity.class);
            startActivity(intent);
        }
    }
}
