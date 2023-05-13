package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EthnicityDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public EthnicityDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getAll() {
        return firebaseFirestore.collection("ethnicity").get();
    }
}
