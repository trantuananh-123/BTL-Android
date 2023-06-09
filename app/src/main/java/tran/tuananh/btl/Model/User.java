package tran.tuananh.btl.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String address;
    private String birthday;
    private String gender;
    private String identificationCard;

    // 0: admin, 1: user, 2: doctor
    private Integer roleType = 1;
    private Province province;
    private District district;
    private Ward ward;
    private String village;
    private String specialistId;
    private Specialist specialist;
    private String healthFacilityId;
    private HealthFacility healthFacility;
    private Ethnicity ethnicity;
    private Job job;
    private Nationality nationality;
    private Integer experience;

    public User() {
    }

    public User(String id, String name, String phone, String email, String avatar, String address, String birthday, String gender, String identificationCard, Integer roleType, Province province, District district, Ward ward, String village, String specialistId, Specialist specialist, String healthFacilityId, HealthFacility healthFacility, Ethnicity ethnicity, Job job, Nationality nationality, Integer experience) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.identificationCard = identificationCard;
        this.roleType = roleType;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.village = village;
        this.specialistId = specialistId;
        this.specialist = specialist;
        this.healthFacilityId = healthFacilityId;
        this.healthFacility = healthFacility;
        this.ethnicity = ethnicity;
        this.job = job;
        this.nationality = nationality;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentificationCard() {
        return identificationCard;
    }

    public void setIdentificationCard(String identificationCard) {
        this.identificationCard = identificationCard;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public HealthFacility getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(String healthFacilityId) {
        this.healthFacilityId = healthFacilityId;
    }

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }
}
