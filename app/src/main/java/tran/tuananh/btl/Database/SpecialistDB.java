package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SpecialistDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public SpecialistDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getById(Integer id) {
        return firebaseFirestore.collection("specialist").whereEqualTo("id", id).get();
    }
}
