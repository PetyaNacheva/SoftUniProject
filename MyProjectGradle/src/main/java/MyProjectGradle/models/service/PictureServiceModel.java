package MyProjectGradle.models.service;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.UserEntity;

public class PictureServiceModel {
    private String id;
    private String title;
    private String url;
    private String publicId; //from Cloudinary
    private String user;
    private String apartment;

    public PictureServiceModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
