package MyProjectGradle.models.binding;

import MyProjectGradle.models.enums.TypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ApartmentUpdateBindingModel {
    private Long id;
    @NotBlank(message = "apartment name is required")
    @Size(min = 3, max=50, message = "name must be at least 3 chars")
    private String name;
    @NotNull(message = "price could not be empty")
    @DecimalMin(value = "0", message ="price must be positive")
    private BigDecimal price;
    @NotBlank(message = "apartment address is required")
    @Size(min = 3, max =50, message ="address must be at least 5 chars" )
    private String address;
    private String type;
    private String town;

    public ApartmentUpdateBindingModel() {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
