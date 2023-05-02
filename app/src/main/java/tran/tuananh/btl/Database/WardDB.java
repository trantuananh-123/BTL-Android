package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class WardDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public WardDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getAllByProvinceAndDistrict(String provinceCode, String districtCode) {
        return firebaseFirestore.collection("ward")
                .whereEqualTo(FieldPath.of("province", "code"), provinceCode)
                .whereEqualTo(FieldPath.of("district", "code"), districtCode)
                .get();
    }
}
