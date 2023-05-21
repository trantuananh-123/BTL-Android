package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public NotificationDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> getPatientNotification(String patientId) {
        return firebaseFirestore.collection("notification").whereEqualTo("patientId", patientId).get();
    }
}
