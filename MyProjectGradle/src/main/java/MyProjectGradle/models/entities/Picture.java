package MyProjectGradle.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{
    @Column(nullable = false)
    private String title;
    @Lob
    private String url;
    @Lob
    private String publicId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "apartment_name")
    private String apartmentName;


    public Picture() {
    }

    public Picture( String url, String publicId, String title, String username) {
        this.title = title;
        this.url = url;
        this.userName = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
}
