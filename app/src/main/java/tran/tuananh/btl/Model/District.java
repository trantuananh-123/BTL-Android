package tran.tuananh.btl.Model;

import java.io.Serializable;

public class District implements Serializable {

    private Long id;
    private String code;
    private String name;

    private Province province;

    public District() {
    }

    public District(Long id, String code, String name, Province province) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.province = province;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
