package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DistrictDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public DistrictDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getAllByProvince(String provinceCode) {
        return firebaseFirestore.collection("district").whereEqualTo(FieldPath.of("province", "code"), provinceCode).get();
    }
}
