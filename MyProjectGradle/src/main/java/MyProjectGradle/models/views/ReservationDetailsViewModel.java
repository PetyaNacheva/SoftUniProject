package MyProjectGradle.models.views;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservationDetailsViewModel {
    private Long id;
    private String username;
    private String guestName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer numberOfGuests;
    private BigDecimal price;
    private LocalDate reservedOn;
    private Apartment apartment;
    private String pictureUrl;
    private boolean canDelete;
    private boolean canUpdate;

    public ReservationDetailsViewModel() {
        canDelete=false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
