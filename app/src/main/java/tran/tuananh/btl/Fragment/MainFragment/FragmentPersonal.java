package tran.tuananh.btl.Fragment.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Activity.LoginActivity;
import tran.tuananh.btl.Activity.PersonalInfoActivity;
import tran.tuananh.btl.Adapter.PersonalRcvAdapter;
import tran.tuananh.btl.Database.MenuDB;
import tran.tuananh.btl.Model.Menu;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class FragmentPersonal extends Fragment implements ViewHolderListener, View.OnClickListener {

    private ProgressBar progressBar;
    private View progressBarBackground;
    private ImageView avatar;
    private TextView fullName, phone;
    private ImageButton btnSignOut;
    private RecyclerView recyclerView;
    private PersonalRcvAdapter personalRcvAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private MenuDB menuDB;
    private List<Menu> menuList = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        initData();
        firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser == null) {
//            startActivity(new Intent(this.getContext(), LoginActivity.class));
//        } else {
//            fullName.setText(firebaseUser.getDisplayName());
//            Query query = firebaseFirestore.collection("user").whereEqualTo("email", firebaseUser.getEmail()).limit(1);
//            query.get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if (document != null) {
//                            phone.setText((String) document.get("phone"));
//                        }
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this.getContext(), LoginActivity.class));
        } else {
            fullName.setText(firebaseUser.getDisplayName());
            Query query = firebaseFirestore.collection("user").whereEqualTo("email", firebaseUser.getEmail()).limit(1);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {
                            phone.setText((String) document.get("phone"));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        avatar = view.findViewById(R.id.avatar);
        fullName = view.findViewById(R.id.fullName);
        phone = view.findViewById(R.id.phone);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarBackground = view.findViewById(R.id.progressBarBackground);
        recyclerView = view.findViewById(R.id.recyclerView);
        personalRcvAdapter = new PersonalRcvAdapter(this.getContext());
        personalRcvAdapter.setViewHolderListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        menuDB = new MenuDB(this.getContext(), firebaseFirestore);
    }

    private void initListener() {
        btnSignOut.setOnClickListener(this);
    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        progressBarBackground.setVisibility(View.VISIBLE);

        Task<QuerySnapshot> task = menuDB.getAll();

        task.addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshotList = task1.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                            Menu menu = new Menu();
                            menu.setId((Long) documentSnapshot.get("id"));
                            menu.setImage((String) documentSnapshot.get("image"));
                            menu.setName((String) documentSnapshot.get("name"));
                            menuList.add(menu);

                            personalRcvAdapter.setMenuList(menuList);
                            recyclerView.setAdapter(personalRcvAdapter);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    FancyToast.makeText(requireContext(), "Get menu failed.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressBar.setVisibility(View.GONE);
                    progressBarBackground.setVisibility(View.GONE);
                });
    }

    @Override
    public void onClickPersonalRcvHolder(View view, int position) {
        Menu menu = personalRcvAdapter.getMenu(position);
        if (menu.getName().equalsIgnoreCase("Personal Information")) {
            Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
            intent.putExtra("firebaseUser", firebaseUser);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnSignOut) {
            if (firebaseAuth != null) {
                firebaseAuth.signOut();
                startActivity(new Intent(this.getContext(), LoginActivity.class));
            }
        }
    }
}
