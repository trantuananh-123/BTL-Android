package tran.tuananh.btl.Model;

import java.io.Serializable;
import java.util.List;

public class HealthFacility implements Serializable {

    private String id;
    private String code;
    private String name;
    private String address;
    private String image;
    private String email;
    private String phone;
    private String website;
    private String fanpage;
    private Province province;
    private District district;
    private Ward ward;
    private List<String> serviceIds;
    private List<String> specialistIds;

    public HealthFacility() {
    }

    public HealthFacility(String id, String code, String name, String address, String image, String email, String phone, String website, String fanpage, Province province, District district, Ward ward) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.fanpage = fanpage;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFanpage() {
        return fanpage;
    }

    public void setFanpage(String fanpage) {
        this.fanpage = fanpage;
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

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public List<String> getSpecialistIds() {
        return specialistIds;
    }

    public void setSpecialistIds(List<String> specialistIds) {
        this.specialistIds = specialistIds;
    }
}
