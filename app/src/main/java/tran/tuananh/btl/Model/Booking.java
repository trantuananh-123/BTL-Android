package tran.tuananh.btl.Model;

import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {

    private String id;
    private String examinationDate;
    private String examinationHour;
    private String specialistId;
    private List<String> serviceIdList;
    private String doctorId;
    private String patientId;
    private String patientName;
    private String healthFacilityId;
    private String symptom;

    public Booking() {
    }

    public Booking(String id, String examinationDate, String examinationHour, String specialistId, List<String> serviceIdList, String doctorId, String patientId, String patientName, String healthFacilityId, String symptom) {
        this.id = id;
        this.examinationDate = examinationDate;
        this.examinationHour = examinationHour;
        this.specialistId = specialistId;
        this.serviceIdList = serviceIdList;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.healthFacilityId = healthFacilityId;
        this.symptom = symptom;
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

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public void setServiceIdList(List<String> serviceIdList) {
        this.serviceIdList = serviceIdList;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(String healthFacilityId) {
        this.healthFacilityId = healthFacilityId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


}
