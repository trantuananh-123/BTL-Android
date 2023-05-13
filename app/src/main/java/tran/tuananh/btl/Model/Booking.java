package tran.tuananh.btl.Model;

import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {

    private Long id;
    private String examinationDate;
    private String examinationHour;
    private Long specialistId;
    private List<Long> serviceIdList;
    private String doctorId;
    private String patientId;
    private String patientName;
    private Long healthFacilityId;
    private String symptom;

    public Booking() {
    }

    public Booking(Long id, String examinationDate, String examinationHour, Long specialistId, List<Long> serviceIdList, String doctorId, String patientId, String patientName, Long healthFacilityId, String symptom) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    public List<Long> getServiceIdList() {
        return serviceIdList;
    }

    public void setServiceIdList(List<Long> serviceIdList) {
        this.serviceIdList = serviceIdList;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Long getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(Long healthFacilityId) {
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
