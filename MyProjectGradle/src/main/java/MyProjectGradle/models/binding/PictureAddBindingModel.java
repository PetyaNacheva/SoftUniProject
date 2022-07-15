package MyProjectGradle.models.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class PictureAddBindingModel {
    private Long id;
    @NotNull(message = "apartment picture is required")
    private MultipartFile picture;
    private String username;
    private String apartmentName;

    public PictureAddBindingModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
}
