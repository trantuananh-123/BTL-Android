package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HealthFacilityDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public HealthFacilityDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getByProvince(String provinceCode) {
        if (provinceCode == null || provinceCode.isEmpty()) {
            return firebaseFirestore.collection("healthFacility").get();
        }
        return firebaseFirestore.collection("healthFacility").whereEqualTo("province.code", provinceCode).get();
    }

    public Task<QuerySnapshot> getById(String id) {
        return firebaseFirestore.collection("healthFacility").whereEqualTo("id", id).get();
    }

    public Task<QuerySnapshot> getAll() {
        return firebaseFirestore.collection("healthFacility").get();
    }
}
