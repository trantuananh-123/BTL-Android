package tran.tuananh.btl.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Specialist implements Serializable {

    private String id;
    private String code;
    private String name;

    public Specialist() {
    }

    public Specialist(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
