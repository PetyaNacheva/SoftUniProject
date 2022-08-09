package MyProjectGradle.models.binding;

import MyProjectGradle.models.enums.TypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;


public class ApartmentAddBindingModel {
    private Long id;
    @NotBlank(message = "apartment name is required")
    @Size(min = 3, max=50, message = "name must be at least 3 chars")
    private String name;
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "apartment type is required")
    private TypeEnum type;
    @NotNull(message = "price could not be empty")
    @DecimalMin(value = "0", message ="price must be positive")
    private BigDecimal price;
    @NotBlank(message = "apartment town is required")
    private String town;
    @NotBlank(message = "apartment address is required")
    @Size(min = 3, max =50, message ="addres must be at least 5 chars" )
    private String address;
    @NotNull(message = "apartment picture is required")
    private MultipartFile picture;


    public ApartmentAddBindingModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
