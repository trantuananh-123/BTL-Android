package tran.tuananh.btl.Fragment.HealthFacilityDetailFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tran.tuananh.btl.Activity.BookingActivity;
import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Model.HealthFacility;
import tran.tuananh.btl.R;

public class FragmentInfo extends Fragment implements View.OnClickListener {

    private ShapeableImageView logo;
    private TextView name1, name2, address1, address2, phone, email, website, fanpage;
    private MaterialButton btnBookingAppointment;
    private HealthFacility healthFacility;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_healthfacility_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

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
        logo = view.findViewById(R.id.logo);
        name1 = view.findViewById(R.id.name1);
        name2 = view.findViewById(R.id.name2);
        address1 = view.findViewById(R.id.address1);
        address2 = view.findViewById(R.id.address2);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        website = view.findViewById(R.id.website);
        fanpage = view.findViewById(R.id.fanpage);
        btnBookingAppointment = view.findViewById(R.id.btnBookingAppointment);
    }

    private void initData(View view) {
        if (firebaseUser == null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        Intent intent = requireActivity().getIntent();
        healthFacility = (HealthFacility) intent.getSerializableExtra("healthFacility");
        if (healthFacility.getImage() != null) {
            Glide.with(view).load(healthFacility.getImage()).into(this.logo);
        }
        name1.setText(healthFacility.getName() != null ? healthFacility.getName() : "");
        name2.setText(healthFacility.getName() != null ? healthFacility.getName() : "");
        address1.setText(healthFacility.getAddress() != null ? healthFacility.getAddress() : "");
        address2.setText(healthFacility.getAddress() != null ? healthFacility.getAddress() : "");
        phone.setText(healthFacility.getPhone() != null ? healthFacility.getPhone() : "");
        email.setText(healthFacility.getEmail() != null ? healthFacility.getEmail() : "");
        website.setText(healthFacility.getWebsite() != null ? healthFacility.getWebsite() : "");
        fanpage.setText(healthFacility.getFanpage() != null ? healthFacility.getFanpage() : "");
    }

    private void initListener() {
        this.website.setOnClickListener(this);
        this.fanpage.setOnClickListener(this);
        this.btnBookingAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == website) {
            Uri uri = Uri.parse(healthFacility.getWebsite());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view == fanpage) {
            Uri uri = Uri.parse(healthFacility.getFanpage());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view == btnBookingAppointment) {
            Intent intent = new Intent(getContext(), BookingActivity.class);
            intent.putExtra("healthFacility", healthFacility);
            startActivity(intent);
        }
    }
}
