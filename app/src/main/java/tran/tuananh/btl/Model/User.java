package tran.tuananh.btl.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String birthday;
    private String gender;
    private String identificationCard;

    // 0: admin, 1: user, 2: doctor
    private Integer type = 0;
    private Province province;
    private District district;
    private Ward ward;
    private String village;

    public User() {
    }

    public User(String id, String fullName, String phone, String email, String address, String birthday, String gender, String identificationCard, Integer type, Province province, District district, Ward ward, String village) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.identificationCard = identificationCard;
        this.type = type;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.village = village;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
