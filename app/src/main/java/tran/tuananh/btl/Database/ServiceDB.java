package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ServiceDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public ServiceDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getById(String id) {
        return firebaseFirestore.collection("service").whereEqualTo("id", id).get();
    }

    public Task<DocumentSnapshot> getById2(String id) {
        return firebaseFirestore.collection("service").document(id).get();
    }

    public Task<QuerySnapshot> getBySpecialist(List<String> specialistIdList) {
//        Query query = firebaseFirestore.collection("service");
//        for (String id : specialistIdList) {
//            query = query.whereArrayContains("specialistIds", id);
//        }
        return firebaseFirestore.collection("service").whereArrayContainsAny("specialistIds", specialistIdList).get();
    }

    public Task<QuerySnapshot> getAll() {
        return firebaseFirestore.collection("service").get();
    }

}
