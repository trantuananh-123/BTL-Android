package tran.tuananh.btl.Model;

import java.io.Serializable;

public class Notification implements Serializable {

    private String id;
    private String examinationDate;
    private String examinationHour;
    private String doctorId;
    private String patientId;
    private String healthFacilityId;
    private String message;

    public Notification() {
    }

    public Notification(String id, String examinationDate, String examinationHour, String doctorId, String patientId, String healthFacilityId, String message) {
        this.id = id;
        this.examinationDate = examinationDate;
        this.examinationHour = examinationHour;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.healthFacilityId = healthFacilityId;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(String examinationDate) {
        this.examinationDate = examinationDate;
    }

    public String getExaminationHour() {
        return examinationHour;
    }

    public void setExaminationHour(String examinationHour) {
        this.examinationHour = examinationHour;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(String healthFacilityId) {
        this.healthFacilityId = healthFacilityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
