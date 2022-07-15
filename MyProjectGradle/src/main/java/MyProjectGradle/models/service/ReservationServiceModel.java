package MyProjectGradle.models.service;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.UserEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationServiceModel {
    private Long id;
    private UserEntity username;
    private String guestName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer numberOfGuests;
    private LocalDate reservedOn;
    private Apartment apartment;
    private BigDecimal price;

    public ReservationServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUsername() {
        return username;
    }

    public void setUsername(UserEntity username) {
        this.username = username;
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

    public LocalDate getReservedOn() {
        return reservedOn;
    }

    public void setReservedOn(LocalDate reservedOn) {
        this.reservedOn = reservedOn;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
