package MyProjectGradle.models.binding;



import MyProjectGradle.models.validation.ValidationDates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.time.LocalDate;

@ValidationDates(first = "arrivalDate",second = "departureDate")
public class ReservationCreateBindingModel {
    @NotBlank(message = "Guest name is required")
    @Size(min=3, max=50, message = "guest name must be between 3 and 50 symbols")
    private String guestName;
    @NotNull(message = "Date must be chosen")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate arrivalDate;
    @NotNull(message = "Date must be chosen")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate departureDate;
    @Positive(message = "guest must be positive number")
    private Integer numberOfGuests;


    public ReservationCreateBindingModel() {
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
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

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
