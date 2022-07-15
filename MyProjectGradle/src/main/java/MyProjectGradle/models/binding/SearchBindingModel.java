package MyProjectGradle.models.binding;

import MyProjectGradle.models.validation.ValidationDates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ValidationDates(first = "arrivalDate",second = "departureDate")
public class SearchBindingModel {
    @NotBlank(message = "please select valid Town name")
    private String town;
    @NotNull(message = "Date must be chosen")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate arrivalDate;
    @NotNull(message = "Date must be chosen")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate departureDate;

    public SearchBindingModel() {
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
