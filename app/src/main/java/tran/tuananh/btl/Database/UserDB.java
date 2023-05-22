package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDB {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public UserDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getDoctorByHealthFacility(String id) {
        return firebaseFirestore.collection("user")
                .whereEqualTo("healthFacilityId", id).whereEqualTo("roleType", 2).get();
    }

    public Task<QuerySnapshot> getDoctorByHealthFacilityAndSpecialist(String healthFacilityId, String specialistId) {
        return firebaseFirestore.collection("user")
                .whereEqualTo("healthFacilityId", healthFacilityId)
                .whereEqualTo("specialistId", specialistId)
                .whereEqualTo("roleType", 2).get();
    }

    public Task<QuerySnapshot> getDoctorById(String id) {
        return firebaseFirestore.collection("user")
                .whereEqualTo("id", id).get();
    }

    public Task<DocumentSnapshot> getById(String id) {
        return firebaseFirestore.collection("user").document(id).get();
    }
}
