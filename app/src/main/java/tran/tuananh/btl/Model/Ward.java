package tran.tuananh.btl.Model;

import java.io.Serializable;

public class Ward implements Serializable {

    private String id;
    private String code;
    private String name;
    private Province province;
    private District district;

    public Ward() {
    }

    public Ward(String id, String code, String name, Province province, District district) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.province = province;
        this.district = district;
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
}
