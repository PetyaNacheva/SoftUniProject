package MyProjectGradle.models.binding;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TownAddBindingModel {
    @NotBlank(message = "town name is required")
    @Size(min = 3, max=50, message = "name must be at least 3 chars")
    private String name;
    @NotBlank(message = "description is required")
    private String description;
    private MultipartFile picture;

    public TownAddBindingModel() {
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

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
