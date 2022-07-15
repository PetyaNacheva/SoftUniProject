package MyProjectGradle.models.views;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.UserEntity;

import java.time.LocalDate;

public class ReservationViewModel {
    private Long id;
    private UserEntity user;
    private String guestName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private ApartmentDetailsViewModel apartment;

    public ReservationViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public ApartmentDetailsViewModel getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentDetailsViewModel apartment) {
        this.apartment = apartment;
    }
}
