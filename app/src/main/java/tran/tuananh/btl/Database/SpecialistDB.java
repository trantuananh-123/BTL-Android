package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SpecialistDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public SpecialistDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getById(String id) {
        return firebaseFirestore.collection("specialist").whereEqualTo("id", id).get();
    }

    public Task<DocumentSnapshot> getById2(String id) {
        return firebaseFirestore.collection("specialist").document(id).get();
    }

    public Task<QuerySnapshot> getAll() {
        return firebaseFirestore.collection("specialist").get();
    }
}
