package tran.tuananh.btl.Database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class BookingDB {

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public BookingDB(Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    public Task<QuerySnapshot> search(String doctorId, String healthFacilityId, String specialistId, String examinationDate) {
        if (specialistId == null) {
            return firebaseFirestore.collection("booking")
                    .whereEqualTo("healthFacilityId", healthFacilityId)
                    .whereEqualTo("examinationDate", examinationDate)
                    .get();
        }
        if (doctorId == null) {
            return firebaseFirestore.collection("booking")
                    .whereEqualTo("healthFacilityId", healthFacilityId)
                    .whereEqualTo("specialistId", specialistId)
                    .whereEqualTo("examinationDate", examinationDate)
                    .get();
        }
        return firebaseFirestore.collection("booking")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("healthFacilityId", healthFacilityId)
                .whereEqualTo("specialistId", specialistId)
                .whereEqualTo("examinationDate", examinationDate)
                .get();
    }

    public AggregateQuery isExist(String doctorId, String healthFacilityId, String specialistId, String examinationDate, String examinationHour) {
        if (doctorId == null) {
            return firebaseFirestore.collection("booking")
                    .whereEqualTo("healthFacilityId", healthFacilityId)
                    .whereEqualTo("specialistId", specialistId)
                    .whereEqualTo("examinationDate", examinationDate)
                    .whereEqualTo("examinationHour", examinationHour)
                    .count();
        }
        return firebaseFirestore.collection("booking")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("healthFacilityId", healthFacilityId)
                .whereEqualTo("specialistId", specialistId)
                .whereEqualTo("examinationDate", examinationDate)
                .whereEqualTo("examinationHour", examinationHour)
                .count();
    }

    public Task<QuerySnapshot> getAll() {
        return firebaseFirestore.collection("booking").get();
    }

    public Task<QuerySnapshot> getByUserId(String id) {
        return firebaseFirestore.collection("booking").whereEqualTo("patientId", id).get();
    }
}
