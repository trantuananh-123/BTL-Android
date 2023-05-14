package tran.tuananh.btl.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Service implements Serializable {

    private String id;
    private String code;
    private String name;
    private Double price;
    private List<String> specialistIds;

    public Service() {
    }

    public Service(String id, String code, String name, Double price, List<String> specialistIds) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.specialistIds = specialistIds;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getSpecialistIds() {
        return specialistIds;
    }

    public void setSpecialistIds(List<String> specialistIds) {
        this.specialistIds = specialistIds;
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + " - " + getName();
    }
}
