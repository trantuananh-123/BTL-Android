package tran.tuananh.btl.Model;

import java.io.Serializable;

public class Menu implements Serializable {

    private Long id;
    private String image;
    private String name;

    public Menu() {
    }

    public Menu(Long id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
