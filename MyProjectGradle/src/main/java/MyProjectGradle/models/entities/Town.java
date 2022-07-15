package MyProjectGradle.models.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "town")
    private List<Apartment> apartments;
    @Lob
    private String description;
    @OneToOne
    private Picture pictureUrl;

    public Town() {

    }

    public Town(String name, String description ) {
        this.name = name;
        this.apartments=new ArrayList<>();
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(Picture pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
    }
}
